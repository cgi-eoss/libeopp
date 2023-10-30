/*
 * Copyright 2020 The libeopp Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cgi.eoss.eopp.jobgraph

import com.cgi.eoss.eopp.identifier.Identifier
import com.cgi.eoss.eopp.job.*
import com.cgi.eoss.eopp.workflow.Input
import com.cgi.eoss.eopp.workflow.Output
import com.cgi.eoss.eopp.workflow.StepConfiguration
import com.google.common.collect.ListMultimap
import com.google.common.collect.MultimapBuilder
import java.net.URI

/**
 * A configured connection between two [Step]s in the graph, i.e. a mapping of the [StepDataSet] type.
 *
 * This is used as the edge type in the [com.google.common.graph.Network] representing a [JobGraph].
 */
data class DataConnector(
    val sourceStep: String,
    val sourceLabel: String,
    val destStep: String,
    val destLabel: String
)

/**
 * A configured step in the graph, i.e. a mapping of the [StepInstance] type.
 *
 * This is used as the node type in the [com.google.common.graph.Network] representing a [JobGraph].
 */
sealed class Step {
    abstract val jobId: String
    abstract val identifier: String
    abstract val inputs: List<StepInputOrOutput>
    abstract val outputs: List<StepInputOrOutput>
    abstract fun toProtobuf(inEdges: Iterable<DataConnector>, outEdges: Iterable<DataConnector>): StepInstance

    // TODO validate min/max count in these functions
    abstract fun hasValidSinks(successors: Set<Step>): Boolean
    abstract fun hasValidSources(predecessors: Set<Step>): Boolean

    internal fun inputsToProtobuf(inEdges: Iterable<DataConnector>): Iterable<StepDataSet> {
        return inEdges.groupBy { it.destLabel }
            .map {
                StepDataSet.newBuilder()
                    .setStepIdentifier(identifier)
                    .setIdentifier(it.key)
                    .setStepInput(
                        StepInput.newBuilder()
                            .addAllSources(it.value.map { dataConnector ->
                                StepDataSet.newBuilder()
                                    .setStepIdentifier(dataConnector.sourceStep)
                                    .setIdentifier(dataConnector.sourceLabel)
                                    .build()
                            })
                            .build()
                    )
                    .build()
            }
    }

    internal fun outputsToProtobuf(outEdges: Iterable<DataConnector>): Iterable<StepDataSet> {
        return outEdges.groupBy { it.sourceLabel }
            .map {
                StepDataSet.newBuilder()
                    .setStepIdentifier(identifier)
                    .setIdentifier(it.key)
                    .build()
            }
    }

    override fun toString(): String =
        "${javaClass.simpleName}(identifier=$identifier)"
}

/**
 * A [Step] which acts as a source of data for the workflow.
 *
 * InputSteps are orphaned nodes (without predecessors) in the [com.google.common.graph.Network] representing a
 * [JobGraph].
 */
data class InputStep(
    override val jobId: String,
    override val identifier: String,
    override val inputs: List<StepInputOrOutput>,
    override val outputs: List<StepInputOrOutput>,
    val inputIdentifier: String,
    val sourceUris: MutableList<URI> = mutableListOf()
) : Step() {
    constructor(jobId: String, input: Input) : this(
        jobId = jobId,
        identifier = "INPUT-${input.identifier}",
        inputs = emptyList<StepInputOrOutput>(),
        outputs = listOf(StepInputOrOutput(input.identifier, input.minOccurs, input.maxOccurs)),
        inputIdentifier = input.identifier
    )

    override fun toProtobuf(inEdges: Iterable<DataConnector>, outEdges: Iterable<DataConnector>): StepInstance {
        return StepInstance.newBuilder()
            .setJobUuid(jobId)
            .setIdentifier(identifier)
            .setType(StepInstance.Type.INPUT)
            .addAllInputs(sourceUrisToProtobuf())
            .addAllOutputs(outputsToProtobuf(outEdges))
            .build()
    }

    override fun hasValidSources(predecessors: Set<Step>): Boolean =
        true // InputSteps require no validation of sources

    override fun hasValidSinks(successors: Set<Step>): Boolean =
        successors.isNotEmpty() // InputSteps must be connected to a sink

    private fun sourceUrisToProtobuf(): List<StepDataSet> {
        return listOf(
            StepDataSet.newBuilder()
                .setStepIdentifier(identifier)
                .setIdentifier(inputIdentifier)
                .setUriList(StepInputUriList.newBuilder().addAllUris(sourceUris.map { it.toString() }).build())
                .build()
        )
    }

    override fun toString(): String = super.toString()
}

/**
 * A configured processing [Step] in the workflow.
 *
 * ProcessSteps are nodes in the [com.google.common.graph.Network] representing a [JobGraph].
 */
data class ProcessStep(
    override val jobId: String,
    override val identifier: String,
    override val inputs: List<StepInputOrOutput>,
    override val outputs: List<StepInputOrOutput>,
    val parameters: List<StepParameter>,
    val parallelParameters: Set<String>,
    val parallelInputs: Set<String>,
    val nodeIdentifier: Identifier,
    val nodeIsNestedWorkflow: Boolean,
    val resourceRequests: com.cgi.eoss.eopp.workflow.Step.Requests,
    val parameterValues: ListMultimap<String, String> = MultimapBuilder.hashKeys().arrayListValues().build()
) : Step() {
    constructor(jobId: String, stepConfiguration: StepConfiguration) : this(
        jobId = jobId,
        identifier = stepConfiguration.identifier,
        inputs = when (stepConfiguration.executeCase) {
            StepConfiguration.ExecuteCase.STEP -> stepConfiguration.step.inputsList
            StepConfiguration.ExecuteCase.NESTED_WORKFLOW -> stepConfiguration.nestedWorkflow.inputsList
            else -> error(ILLEGAL_STEP_CASE)
        }.map {
            StepInputOrOutput(
                it.identifier,
                it.minOccurs,
                it.maxOccurs
            )
        },
        outputs = when (stepConfiguration.executeCase) {
            StepConfiguration.ExecuteCase.STEP -> stepConfiguration.step.outputsList
            StepConfiguration.ExecuteCase.NESTED_WORKFLOW -> stepConfiguration.nestedWorkflow.outputsList
            else -> error(ILLEGAL_STEP_CASE)
        }.map {
            StepInputOrOutput(
                it.identifier,
                it.minOccurs,
                it.maxOccurs
            )
        },
        parameters = when (stepConfiguration.executeCase) {
            StepConfiguration.ExecuteCase.STEP -> stepConfiguration.step.parametersList
            StepConfiguration.ExecuteCase.NESTED_WORKFLOW -> stepConfiguration.nestedWorkflow.parametersList
            else -> error(ILLEGAL_STEP_CASE)
        }.map {
            StepParameter(
                it.identifier,
                it.minOccurs,
                if (it.maxOccurs > 0) it.maxOccurs else Int.MAX_VALUE,
                it.defaultValuesList,
                stepConfiguration.parameterLinksList.find { pl -> pl.identifier == it.identifier }?.skipStepIfEmpty
                    ?: false
            )
        },
        parallelParameters = stepConfiguration.parameterLinksList.filter { it.parallel }.map { it.identifier }.toSet(),
        parallelInputs = stepConfiguration.inputLinksList.filter { it.parallel }.map { it.identifier }.toSet(),
        nodeIdentifier = when (stepConfiguration.executeCase) {
            StepConfiguration.ExecuteCase.STEP -> stepConfiguration.step.identifier
            StepConfiguration.ExecuteCase.NESTED_WORKFLOW -> stepConfiguration.nestedWorkflow.identifier
            else -> error(ILLEGAL_STEP_CASE)
        },
        nodeIsNestedWorkflow = stepConfiguration.hasNestedWorkflow(),
        resourceRequests = stepConfiguration.step.requests
    )

    override fun toProtobuf(inEdges: Iterable<DataConnector>, outEdges: Iterable<DataConnector>): StepInstance {
        return with(
            StepInstance.newBuilder()
                .setJobUuid(jobId)
                .setIdentifier(identifier)
                .setType(StepInstance.Type.PROCESS)
                .addAllParameters(parameterValuesToProtobuf())
                .addAllInputs(inputsToProtobuf(inEdges))
                .addAllOutputs(outputsToProtobuf(outEdges))
        ) {
            if (nodeIsNestedWorkflow) {
                this.setWorkflowIdentifier(nodeIdentifier)
            } else {
                this.setStepIdentifier(nodeIdentifier)
            }
            this
        }.build()
    }

    override fun hasValidSources(predecessors: Set<Step>): Boolean =
        inputs.isEmpty() || predecessors.isNotEmpty() // if inputs is not empty, predecessor steps must exist

    override fun hasValidSinks(successors: Set<Step>): Boolean =
        outputs.isEmpty() || successors.isNotEmpty() // if outputs is not empty, successor steps must exist

    private fun parameterValuesToProtobuf(): Iterable<StepParameterValue> =
        parameters.map {
            StepParameterValue.newBuilder()
                .setIdentifier(it.identifier)
                .addAllValues(parameterValues[it.identifier]) // JobGraph#build handles defaults
                .build()
        }

    override fun toString(): String = super.toString()
}

/**
 * A [Step] which acts as a sink of data for the workflow.
 *
 * OutputSteps are terminal nodes (without successors) in the [com.google.common.graph.Network] representing a
 * [JobGraph].
 */
data class OutputStep(
    override val jobId: String,
    override val identifier: String,
    override val inputs: List<StepInputOrOutput>,
    override val outputs: List<StepInputOrOutput>,
    val outputIdentifier: String
) : Step() {
    constructor(jobId: String, output: Output) : this(
        jobId = jobId,
        identifier = "OUTPUT-${output.identifier}",
        inputs = listOf(StepInputOrOutput(output.identifier, output.minOccurs, output.maxOccurs)),
        outputs = emptyList<StepInputOrOutput>(),
        outputIdentifier = output.identifier
    )

    override fun toProtobuf(inEdges: Iterable<DataConnector>, outEdges: Iterable<DataConnector>): StepInstance {
        return StepInstance.newBuilder()
            .setJobUuid(jobId)
            .setIdentifier(identifier)
            .setType(StepInstance.Type.OUTPUT)
            .addAllInputs(inputsToProtobuf(inEdges))
            .build()
    }

    override fun hasValidSources(predecessors: Set<Step>): Boolean =
        predecessors.isNotEmpty() // OutputSteps must be connected to a source

    override fun hasValidSinks(successors: Set<Step>): Boolean =
        true // OutputSteps require no validation of sinks

    override fun toString(): String = super.toString()
}

/**
 * Minimal schema of a step's input or output data parameter. A simplified representation of
 * [com.cgi.eoss.eopp.workflow.Input] or [com.cgi.eoss.eopp.workflow.Output].
 */
data class StepInputOrOutput(
    val identifier: String,
    val minOccurs: Int = 0,
    val maxOccurs: Int = Int.MAX_VALUE,
    val skipStepIfEmpty: Boolean = false
)

/**
 * Minimal schema of a step's parameter. A simplified representation a [com.cgi.eoss.eopp.workflow.Parameter].
 */
data class StepParameter(
    val identifier: String,
    val minOccurs: Int = 0,
    val maxOccurs: Int = Int.MAX_VALUE,
    val defaultValues: List<String>,
    val skipStepIfEmpty: Boolean = false
)

private const val ILLEGAL_STEP_CASE = "stepConfiguration.executeCase must be STEP or NESTED_WORKFLOW"
