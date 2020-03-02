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

import com.cgi.eoss.eopp.job.StepDataSet
import com.cgi.eoss.eopp.job.StepDataSets.createStepOutputURI
import com.cgi.eoss.eopp.job.StepDataSets.fromStepOutputURI
import com.cgi.eoss.eopp.job.StepInput
import com.cgi.eoss.eopp.job.StepInstance
import com.cgi.eoss.eopp.job.StepInstances
import com.cgi.eoss.eopp.job.StepInstances.getStepParameterIdx
import com.cgi.eoss.eopp.job.StepOutput
import com.cgi.eoss.eopp.job.StepParameterValue
import com.cgi.eoss.eopp.workflow.StepConfiguration
import com.cgi.eoss.eopp.workflow.StepConfiguration.InputLink
import com.cgi.eoss.eopp.workflow.Workflow
import org.springframework.util.AntPathMatcher
import java.net.URI
import java.util.UUID

// Utility functions for expanding StepInstances with multiplicity

private val ANT_PATH_MATCHER = AntPathMatcher()

/**
 * Find all step inputs which are configured as parallel, and expand them to produce one new StepInstance per value.
 */
internal fun expandParallelInputs(step: StepInstance): List<StepInstance> = sequenceOf(step)
    .flatMap { it.configuration.inputLinksList.asSequence() }
    .filter { it.parallel }
    .flatMap { stepInputLink ->
        sequenceOf(stepInputLink)
            .map { it.identifier }
            .map { StepInstances.getStepInput(step, it) }
            .flatMap { stepInput ->
                stepInput.stepInput.sourcesList.asSequence()
                    .flatMap { sourceDataSet ->
                        sourceDataSet.stepOutput.filePathsList
                            .map { StepInstanceDataSource(sourceDataSet.stepIdentifier, sourceDataSet.identifier, it) }
                            .asSequence()
                    }
                    // filter by includes/excludes
                    .filter { applyStepInputLinkFilter(stepInputLink, it.filePath) }
                    // filter by groupBy regex
                    .filter { stepInputLink.groupBy.isEmpty() || Regex(stepInputLink.groupBy).matches(it.filePath) }
                    // apply grouping
                    .groupBy { applyStepInputLinkGroup(stepInputLink, it.filePath) }
                    .map { replaceExpandedStepInput(step, stepInput, it.value) }
                    .asSequence()
            }
    }
    .toList()
    .ifEmpty { listOf(step) }

private data class StepInstanceDataSource(
    val stepIdentifier: String,
    val stepOutputIdentifier: String,
    val filePath: String
)

/**
 * Find all step parameters which are configured as parallel, and expand them to produce one new StepInstance per value.
 */
internal fun expandParallelParameters(step: StepInstance): List<StepInstance> = sequenceOf(step)
    .flatMap { it.configuration.parameterLinksList.asSequence() }
    .filter { it.parallel }
    .flatMap { stepParamLink ->
        sequenceOf(stepParamLink)
            .map { it.identifier }
            .map { StepInstances.getStepParameter(step, it) }
            .flatMap { stepParameter ->
                stepParameter.valuesList
                    .asSequence()
                    .map { paramValue ->
                        StepParameterValue.newBuilder()
                            .setIdentifier(stepParameter.identifier)
                            .addValues(paramValue)
                            .build()
                    }
                    .map { newParamValue ->
                        step.toBuilder()
                            .setParameters(getStepParameterIdx(step, stepParameter.identifier), newParamValue)
                            .build()
                    }
            }
    }
    .toList()
    .ifEmpty { listOf(step) }

internal fun renameExpandedSteps(parentStep: StepInstance, expandedSteps: List<StepInstance>): List<StepInstance> =
    if (expandedSteps.size > 1)
        expandedSteps
            .mapIndexed { idx, stepInstance ->
                stepInstance.toBuilder()
                    .setIdentifier("${stepInstance.identifier}-${padIndex(idx + 1, expandedSteps.size + 1)}")
                    .setParentIdentifier(parentStep.identifier)
                    .build()
            }
    else
        expandedSteps

private fun padIndex(idx: Int, max: Int): String =
    idx.toString().padStart(max.toString().length, '0')

private fun applyStepInputLinkFilter(stepInputLink: InputLink, stepOutputFilePath: String): Boolean {
    // only files that match at least one of the include patterns and don't match any of the exclude patterns are used
    val includesMatch = (stepInputLink.includesList.isEmpty()
            || stepInputLink.includesList.any { ANT_PATH_MATCHER.match(it, stepOutputFilePath) })
    val excludesMatch = (stepInputLink.excludesList.isNotEmpty()
            && stepInputLink.excludesList.any { ANT_PATH_MATCHER.match(it, stepOutputFilePath) })
    return includesMatch && !excludesMatch
}

private fun applyStepInputLinkGroup(stepInputLink: InputLink, stepOutputFilePath: String): String =
    if (stepInputLink.groupBy.isEmpty()) {
        // Ensure we don't group at all
        UUID.randomUUID().toString()
    } else {
        val matchResult = Regex(stepInputLink.groupBy).matchEntire(stepOutputFilePath)!!
        require(matchResult.groups.size == 2) {
            "StepConfiguration.InputLink#groupBy regex did not contain a single capturing group: ${stepInputLink.groupBy}"
        }
        matchResult.groupValues[1]
    }

private fun replaceExpandedStepInput(
    stepInstance: StepInstance,
    stepInput: StepDataSet,
    sourceData: List<StepInstanceDataSource>
): StepInstance = stepInstance.toBuilder()
    .setInputs(
        StepInstances.getStepInputIdx(stepInstance, stepInput.identifier),
        StepInstances.getStepInput(stepInstance, stepInput.identifier)
            .toBuilder()
            .clearConnector()
            .setStepInput(
                StepInput.newBuilder()
                    .addAllSources(
                        sourceData.map {
                            StepDataSet.newBuilder()
                                .setStepIdentifier(it.stepIdentifier)
                                .setIdentifier(it.stepOutputIdentifier)
                                .setStepOutput(StepOutput.newBuilder().addFilePaths(it.filePath).build())
                                .build()
                        }
                    )
                    .build()
            )
    )
    .build()

/**
 * Find all steps which are configured as nested workflows, and expand them to their constituent steps.
 */
internal fun expandNestedWorkflows(step: StepInstance): List<StepInstance> = sequenceOf(step)
    .filter { it.configuration.executeCase == StepConfiguration.ExecuteCase.NESTED_WORKFLOW }
    .flatMap {
        JobGraph.builder(step.jobUuid)
            .withWorkflow(step.configuration.nestedWorkflow)
            .withInputs(step.inputsList
                .flatMap { expandNestedWorkflowInputs(step.jobUuid, it) }
                .groupBy(
                    { it.inputIdentifier },
                    { it.inputURI }
                ))
            .withParameters(StepInstances.parameterValues(step))
            .build()
            .getSteps()
            // Update the step identifiers across all input/output links with the prefixed id
            .map { subStep ->
                subStep.toBuilder()
                    .setIdentifier("${step.identifier}-${subStep.identifier}")
                    .setParentIdentifier(step.identifier)
                    .clearInputs()
                    .addAllInputs(subStep.inputsList.filter { it.hasStepInput() }.map { stepInput ->
                        stepInput.toBuilder()
                            .setStepIdentifier("${step.identifier}-${subStep.identifier}")
                            .setStepInput(
                                stepInput.stepInput.toBuilder()
                                    .clearSources()
                                    .addAllSources(stepInput.stepInput.sourcesList.map { stepInputSource ->
                                        stepInputSource.toBuilder()
                                            .setStepIdentifier("${step.identifier}-${stepInputSource.stepIdentifier}")
                                            .build()
                                    })
                            )
                            .build()
                    })
                    .addAllInputs(subStep.inputsList.filter { it.hasUriList() }.flatMap { stepInput ->
                        stepInput.uriList.urisList
                            .filter { uri -> uri.startsWith("eopp+stepoutput://") }
                            .map { uri ->
                                StepDataSet.newBuilder()
                                    .setStepIdentifier("${step.identifier}-${subStep.identifier}")
                                    .setIdentifier(stepInput.identifier)
                                    .setStepInput(
                                        StepInput.newBuilder().addSources(fromStepOutputURI(URI.create(uri))).build()
                                    )
                                    .build()
                            }
                    })
                    .clearOutputs()
                    .addAllOutputs(subStep.outputsList.map {
                        it.toBuilder()
                            .setStepIdentifier("${step.identifier}-${subStep.identifier}")
                            .build()
                    })
                    .build()
            }
            .asSequence()
    }
    .toList()
    .ifEmpty { listOf(step) }

private fun expandNestedWorkflowInputs(jobUuid: String, stepInput: StepDataSet): List<NestedWorkflowInput> {
    // These will always be step outputs, unlike a top-level Workflow execution
    return stepInput.stepInput.sourcesList
        .flatMap { sourceDataSet ->
            sourceDataSet.stepOutput.filePathsList
                .map { outputFile ->
                    NestedWorkflowInput(
                        stepInput.identifier,
                        createStepOutputURI(jobUuid, sourceDataSet, outputFile)
                    )
                }
        }
}

private data class NestedWorkflowInput(val inputIdentifier: String, val inputURI: URI)
