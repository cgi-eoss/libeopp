package com.cgi.eoss.eopp.jobgraph

import com.cgi.eoss.eopp.job.JobSpecification
import com.cgi.eoss.eopp.job.StepInstance
import com.cgi.eoss.eopp.workflow.Input
import com.cgi.eoss.eopp.workflow.Output
import com.cgi.eoss.eopp.workflow.StepConfiguration
import com.cgi.eoss.eopp.workflow.Workflow
import com.google.common.collect.ListMultimap
import com.google.common.collect.MultimapBuilder
import com.google.common.collect.SetMultimap
import com.google.common.graph.MutableNetwork
import com.google.common.graph.Network
import com.google.common.graph.NetworkBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URI

/**
 * A reprojection of a [com.cgi.eoss.eopp.job.JobSpecification], describing the steps and their connections as a
 * [Network].
 */
class JobGraph private constructor(
    /**
     * The job graph modeled as a [Network].
     */
    val network: Network<Step, DataConnector>
) {
    companion object {
        /**
         * Begin construction of a new [JobGraph], starting with the given job identifier.
         */
        @JvmStatic
        fun builder(jobId: String): JobIdStubbing = Builder(jobId)

        @JvmStatic
        fun expandStepInstance(stepInstance: StepInstance): List<StepInstance> =
            // expand parallel steps first, so we can rename based on the total count (step-01, step-02, etc.)
            renameExpandedSteps(stepInstance,
                listOf(stepInstance)
                    .flatMap { expandParallelInputs(it) }
                    .flatMap { expandParallelParameters(it) }
            )
                // then expand any nested workflows
                .flatMap { expandNestedWorkflows(it) }
    }

    /**
     * @return The job graph serialised as a set of [StepInstance]s.
     */
    fun getSteps(): Set<StepInstance> {
        return network.nodes().map {
            val stepBuilder = it.toProtobuf(network.inEdges(it), network.outEdges(it)).toBuilder()
            stepBuilder.build()
        }.toSet()
    }

    private data class Builder(
        val jobId: String,
        val log: Logger,
        val steps: MutableMap<String, Step> = mutableMapOf(),
        val parameterLinks: SetMultimap<String, Pair<ProcessStep, String>> = MultimapBuilder.hashKeys().hashSetValues()
            .build(),
        val dataConnectors: MutableSet<DataConnector> = mutableSetOf()
    ) : JobIdStubbing, WorkflowStubbing, InputStubbing, BuildStubbing {

        constructor(jobId: String) : this(
            jobId = jobId,
            log = LoggerFactory.getLogger(Builder::class.qualifiedName + "." + jobId)
        )

        override fun with(jobSpec: JobSpecification): BuildStubbing {
            checkNotNull(jobSpec.workflow)

            withWorkflow(jobSpec.workflow)

            jobSpec.parametersList.forEach {
                withParameter(it.identifier, it.valuesList)
            }
            jobSpec.inputsList.forEach {
                withInput(it.identifier, it.valuesList.map { uri -> URI.create(uri) })
            }

            return this
        }

        override fun withWorkflow(): WorkflowStubbing = this
        override fun endWorkflow() = this

        override fun withWorkflow(workflow: Workflow): InputStubbing = apply {
            workflow.inputsList.forEach { withWorkflowInput(it) }
            workflow.stepConfigurationsList.forEach { withStepConfiguration(it) }
            workflow.outputsList.forEach { withWorkflowOutput(it) }
        }

        override fun withWorkflowInput(input: Input): WorkflowStubbing = apply {
            steps["INPUT-${input.identifier}"] = InputStep(jobId, input)
        }

        override fun withStepConfiguration(stepConfiguration: StepConfiguration) = apply {
            val processStep = ProcessStep(jobId, stepConfiguration)
            steps[stepConfiguration.identifier] = processStep
            stepConfiguration.parameterLinksList.forEach {
                if (it.hardcodedValuesCount > 0) {
                    // Set the hardcoded step parameter values
                    processStep.parameterValues.putAll(it.identifier, it.hardcodedValuesList)
                } else {
                    // Prepare to receive parameterValues from the job configuration in #parameter calls
                    parameterLinks.put(it.workflowParameter, Pair(processStep, it.identifier))
                }
            }
            stepConfiguration.inputLinksList.forEach { inputLink ->
                inputLink.sources.workflowInputsList.forEach { workflowInput ->
                    dataConnectors.add(
                        DataConnector(
                            "INPUT-$workflowInput", workflowInput,
                            processStep.identifier, inputLink.identifier
                        )
                    )
                }
                inputLink.sources.stepOutputsList.forEach { stepOutput ->
                    dataConnectors.add(
                        DataConnector(
                            stepOutput.stepIdentifier, stepOutput.outputIdentifier,
                            processStep.identifier, inputLink.identifier
                        )
                    )
                }
            }
        }

        override fun withWorkflowOutput(output: Output) = apply {
            steps["OUTPUT-${output.identifier}"] = OutputStep(jobId, output)
            output.sources.workflowInputsList.forEach { workflowInput ->
                dataConnectors.add(
                    DataConnector(
                        workflowInput, workflowInput,
                        "OUTPUT-${output.identifier}", output.identifier
                    )
                )
            }
            output.sources.stepOutputsList.forEach { stepOutput ->
                dataConnectors.add(
                    DataConnector(
                        stepOutput.stepIdentifier, stepOutput.outputIdentifier,
                        "OUTPUT-${output.identifier}", output.identifier
                    )
                )
            }
        }

        override fun withParameters(parameters: ListMultimap<String, String>) = apply {
            withParameters(parameters.asMap())
        }

        override fun withParameters(parameters: Map<String, Iterable<String>>) = apply {
            parameters.forEach { (key, values) -> withParameter(key, values) }
        }

        override fun withParameter(key: String, values: Iterable<String>) = apply {
            values.forEach { withParameter(key, it) }
        }

        override fun withParameter(key: String, value: String) = apply {
            parameterLinks.get(key).forEach {
                it.first.parameterValues.put(it.second, value)
            }
        }

        override fun withInputs(inputs: ListMultimap<String, URI>) = apply {
            withInputs(inputs.asMap())
        }

        override fun withInputs(inputs: Map<String, Iterable<URI>>) = apply {
            inputs.forEach { (key, values) -> withInput(key, values) }
        }

        override fun withInput(key: String, values: Iterable<URI>) = apply {
            values.forEach { withInput(key, it) }
        }

        override fun withInput(key: String, value: URI) = apply {
            (steps["INPUT-$key"] as InputStep).sourceUris.add(value)
        }

        override fun build(): JobGraph {
            val network = NetworkBuilder.directed()
                .allowsParallelEdges(true).allowsSelfLoops(false)
                .expectedEdgeCount(dataConnectors.size).expectedNodeCount(steps.size)
                .build<Step, DataConnector>()

            // Build the network so we can prune safely
            dataConnectors.forEach {
                network.addEdge(steps[it.sourceStep]!!, steps[it.destStep]!!, it)
            }

            pruneSkippableSteps(network)

            return JobGraph(network)
        }

        private fun pruneSkippableSteps(network: MutableNetwork<Step, DataConnector>) {
            // Prune steps which have unsatisfied parameters, if skippable, or fail accordingly
            steps.values
                .filterIsInstance<ProcessStep>()
                .filter { stepIsSkippableBasedOnParameters(it) }
                .forEach { network.removeNode(it) }

            // Trim steps with no predecessors that are not permitted to be source steps and steps with no successors that are not permitted to be sink steps
            var illegalSources: List<Step> = listOf()
            var illegalSinks: List<Step> = listOf()
            while ({ illegalSources = stepHasNoSources(network); illegalSources }().isNotEmpty()
                || { illegalSinks = stepHasNoSinks(network); illegalSinks }().isNotEmpty()
            ) {
                if (illegalSources.isNotEmpty()) {
                    log.debug("Removing steps without predecessors: {}", illegalSources)
                    illegalSources.forEach { network.removeNode(it) }
                }
                if (illegalSinks.isNotEmpty()) {
                    log.debug("Removing steps without successors: {}", illegalSinks)
                    illegalSinks.forEach { network.removeNode(it) }
                }
            }

            if (network.nodes().isEmpty()) {
                throw GraphBuildFailureException("Network was empty after illegal source and sink step removal")
            }
        }

        private fun stepIsSkippableBasedOnParameters(step: ProcessStep): Boolean {
            return step.parameters.any { param ->
                val parameterValues = step.parameterValues.get(param.identifier)

                if (parameterValues.isEmpty()) { // the job configuration contained no values
                    parameterValues.addAll(param.defaultValues) // use any defaults from the workflow - might still be empty!
                }

                // Prune this step from the execution graph if its parameter says it should be skipped - otherwise the step will run with no value
                if (parameterValues.isEmpty() && param.skipStepIfEmpty) {
                    return true
                }

                // Check the constraints
                if (parameterValues.size < param.minOccurs) {
                    throw GraphBuildFailureException("Parameter '${param.identifier}' for step '${step.identifier}' has too few values: ${parameterValues.size} < ${param.minOccurs}")
                } else if (parameterValues.size > param.maxOccurs && !step.parallelParameters.contains(param.identifier)) {
                    throw GraphBuildFailureException("Parameter '${param.identifier}' for step '${step.identifier}' has too many values: ${parameterValues.size} > ${param.maxOccurs}")
                }

                return false
            }
        }

        private fun stepHasNoSinks(network: MutableNetwork<Step, DataConnector>) =
            network.nodes().filter { !it.hasValidSinks(network.successors(it)) }

        private fun stepHasNoSources(network: MutableNetwork<Step, DataConnector>) =
            network.nodes().filter { !it.hasValidSources(network.predecessors(it)) }
    }
}

interface JobIdStubbing {
    /**
     * Populate a [JobGraph] from the given job spec.
     *
     * Note: The JobSpecification must include the full Workflow object.
     */
    fun with(jobSpec: JobSpecification): BuildStubbing

    /**
     * Begin describing a new Workflow configuration for the JobGraph under construction. Call
     * [WorkflowStubbing#withWorkflowInput], [WorkflowStubbing#withStepConfiguration] and
     * [WorkflowStubbing#withWorkflowOutput].
     *
     * Finalise the Workflow with [WorkflowStubbing#endWorkflow].
     */
    fun withWorkflow(): WorkflowStubbing

    /**
     * Set the given Workflow as describing the JobGraph under construction.
     *
     * Proceed with [InputStubbing#withParameters], [InputStubbing#withParameter], [InputStubbing#withInputs],
     * [InputStubbing#withInput].
     */
    fun withWorkflow(workflow: Workflow): InputStubbing
}

interface WorkflowStubbing {
    /**
     * Add the given [Input] to the JobGraph under construction.
     */
    fun withWorkflowInput(input: Input): WorkflowStubbing

    /**
     * Add the given [StepConfiguration] to the JobGraph under construction.
     */
    fun withStepConfiguration(stepConfiguration: StepConfiguration): WorkflowStubbing

    /**
     * Add the given [Output] to the JobGraph under construction.
     */
    fun withWorkflowOutput(output: Output): WorkflowStubbing

    /**
     * Finalise the Workflow describing the JobGraph under construction.
     *
     * Proceed with [InputStubbing#withParameters], [InputStubbing#withParameter], [InputStubbing#withInputs],
     * [InputStubbing#withInput].
     */
    fun endWorkflow(): InputStubbing
}

interface InputStubbing : BuildStubbing {
    /**
     * Add the given parameter values to the JobGraph under construction.
     */
    fun withParameters(parameters: ListMultimap<String, String>): InputStubbing

    /**
     * Add the given parameter values to the JobGraph under construction.
     */
    fun withParameters(parameters: Map<String, Iterable<String>>): InputStubbing

    /**
     * Add the given parameter values to the JobGraph under construction.
     */
    fun withParameter(key: String, values: Iterable<String>): InputStubbing

    /**
     * Add the given parameter value to the JobGraph under construction.
     */
    fun withParameter(key: String, value: String): InputStubbing

    /**
     * Add the given input values to the JobGraph under construction.
     */
    fun withInputs(inputs: ListMultimap<String, URI>): InputStubbing

    /**
     * Add the given input values to the JobGraph under construction.
     */
    fun withInputs(inputs: Map<String, Iterable<URI>>): InputStubbing

    /**
     * Add the given input values to the JobGraph under construction.
     */
    fun withInput(key: String, values: Iterable<URI>): InputStubbing

    /**
     * Add the given input value to the JobGraph under construction.
     */
    fun withInput(key: String, value: URI): InputStubbing
}

interface BuildStubbing {
    /**
     * Assemble the given Workflow, Inputs and Parameters into a Network, and perform consistency checking and removal
     * of skippable invalid steps.
     */
    fun build(): JobGraph
}
