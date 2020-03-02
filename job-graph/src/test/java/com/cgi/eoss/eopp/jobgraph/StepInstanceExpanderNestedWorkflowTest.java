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

package com.cgi.eoss.eopp.jobgraph;

import com.cgi.eoss.eopp.job.StepDataSet;
import com.cgi.eoss.eopp.job.StepInput;
import com.cgi.eoss.eopp.job.StepInstance;
import com.cgi.eoss.eopp.job.StepOutput;
import com.cgi.eoss.eopp.job.StepParameterValue;
import com.cgi.eoss.eopp.workflow.StepConfiguration;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.truth.Correspondence.from;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.extensions.proto.ProtoTruth.assertThat;

@RunWith(JUnit4.class)
public class StepInstanceExpanderNestedWorkflowTest {

    private StepInstance baseStepInstance = StepInstance.newBuilder()
            .setIdentifier("test-step")
            .setConfiguration(StepConfiguration.newBuilder()
                    .setNestedWorkflow(JobGraphTest.workflow)
                    .build())
            .addParameters(StepParameterValue.newBuilder()
                    .setIdentifier("sourced_param") // matches the workflow parameter
                    .addValues("param-value1")
                    .addValues("param-value2")
                    .addValues("param-value3")
                    .build())
            .addInputs(StepDataSet.newBuilder()
                    .setIdentifier("provided_workflow_input") // matches the workflow input
                    .setStepInput(StepInput.newBuilder()
                            .addSources(StepDataSet.newBuilder()
                                    .setStepIdentifier("source-step")
                                    .setIdentifier("first-source")
                                    .setStepOutput(StepOutput.newBuilder()
                                            .addFilePaths("file1")
                                            .addFilePaths("file2")
                                            .build())
                                    .build())
                            .addSources(StepDataSet.newBuilder()
                                    .setStepIdentifier("source-step")
                                    .setIdentifier("second-source")
                                    .setStepOutput(StepOutput.newBuilder()
                                            .addFilePaths("file1")
                                            .addFilePaths("file2")
                                            .addFilePaths("file3")
                                            .build())
                                    .build())
                            .build())
                    .build())
            .addInputs(StepDataSet.newBuilder()
                    .setIdentifier("unprovided_workflow_input")
                    .setStepInput(StepInput.newBuilder()
                            .addSources(StepDataSet.newBuilder()
                                    .setStepIdentifier("source-step")
                                    .setIdentifier("third-source")
                                    .setStepOutput(StepOutput.newBuilder().build()) // empty data set
                                    .build())
                            .build())
                    .build())
            .build();

    @Test
    public void testExpandNestedWorkflow() {
        StepInstance stepInstance = baseStepInstance;

        List<StepInstance> stepInstances = JobGraph.expandStepInstance(stepInstance);

        assertThat(stepInstances.stream().map(StepInstance::getParentIdentifier).collect(Collectors.toSet()))
                .containsExactly("test-step");

        ImmutableMap<String, StepInstance> steps = Maps.uniqueIndex(stepInstances, StepInstance::getIdentifier);

        assertThat(steps.keySet()).containsExactly(
                "test-step-INPUT-provided_workflow_input",
                "test-step-first-step",
                "test-step-second-step",
                "test-step-skipped-step",
                "test-step-OUTPUT-workflow_output"
        ); // unprovided_workflow_input was removed, along with its descendents

        // all new steps have the same parent reference
        assertThat(steps.values()).comparingElementsUsing(from((StepInstance actual, String expected) -> actual.getParentIdentifier().equals(expected), "Parent identifier is 'test-step'"))
                .containsExactly("test-step", "test-step", "test-step", "test-step", "test-step");

        assertThat(steps.get("test-step-INPUT-provided_workflow_input").getInputsList()).containsExactly(
                getStepConnector("test-step-INPUT-provided_workflow_input", "provided_workflow_input", "source-step", "first-source", "file1"),
                getStepConnector("test-step-INPUT-provided_workflow_input", "provided_workflow_input", "source-step", "first-source", "file2"),
                getStepConnector("test-step-INPUT-provided_workflow_input", "provided_workflow_input", "source-step", "second-source", "file1"),
                getStepConnector("test-step-INPUT-provided_workflow_input", "provided_workflow_input", "source-step", "second-source", "file2"),
                getStepConnector("test-step-INPUT-provided_workflow_input", "provided_workflow_input", "source-step", "second-source", "file3"));

        assertThat(steps.get("test-step-first-step").getInputsList()).containsExactly(
                getStepConnector("test-step-first-step", "in1", "test-step-INPUT-provided_workflow_input", "provided_workflow_input", null));
        assertThat(steps.get("test-step-first-step").getParameters(0)).isEqualTo(StepParameterValue.newBuilder()
                .setIdentifier("SOURCED_PARAM")
                .addValues("param-value1")
                .addValues("param-value2")
                .addValues("param-value3")
                .build());

        assertThat(steps.get("test-step-second-step").getInputsList()).containsExactly(
                getStepConnector("test-step-second-step", "in1", "test-step-first-step", "out1", null));

        assertThat(steps.get("test-step-skipped-step").getInputsList()).containsExactly(
                getStepConnector("test-step-skipped-step", "in1", "test-step-first-step", "out1", null));

        assertThat(steps.get("test-step-OUTPUT-workflow_output").getInputsList()).ignoringRepeatedFieldOrder().containsExactly(
                StepDataSet.newBuilder()
                        .setStepIdentifier("test-step-OUTPUT-workflow_output")
                        .setIdentifier("workflow_output")
                        .setStepInput(StepInput.newBuilder()
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("test-step-second-step")
                                        .setIdentifier("out1")
                                        .build())
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("test-step-skipped-step")
                                        .setIdentifier("out1")
                                        .build())
                                .build())
                        .build()
        );
    }

    private StepDataSet getStepConnector(String stepIdentifier, String identifier, String sourceStepIdentifier, String sourceOutputIdentifier, String filePath) {
        StepDataSet.Builder stepOutput = StepDataSet.newBuilder()
                .setStepIdentifier(sourceStepIdentifier)
                .setIdentifier(sourceOutputIdentifier);
        Optional.ofNullable(filePath)
                .ifPresent(it -> stepOutput.setStepOutput(StepOutput.newBuilder().addFilePaths(it).build()));
        return StepDataSet.newBuilder()
                .setStepIdentifier(stepIdentifier)
                .setIdentifier(identifier)
                .setStepInput(StepInput.newBuilder()
                        .addSources(stepOutput
                                .build())
                        .build())
                .build();
    }

}
