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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static com.google.common.truth.extensions.proto.ProtoTruth.assertThat;

@RunWith(JUnit4.class)
public class StepInstanceExpanderParallelTest {

    private StepInstance baseStepInstance = StepInstance.newBuilder()
            .setIdentifier("test-step")
            .addParameters(StepParameterValue.newBuilder()
                    .setIdentifier("first-param")
                    .addValues("param-value1")
                    .addValues("param-value2")
                    .addValues("param-value3")
                    .build())
            .addInputs(StepDataSet.newBuilder()
                    .setIdentifier("first-input")
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
                    .setIdentifier("second-input")
                    .setStepInput(StepInput.newBuilder()
                            .addSources(StepDataSet.newBuilder()
                                    .setStepIdentifier("source-step")
                                    .setIdentifier("third-source")
                                    .setStepOutput(StepOutput.newBuilder()
                                            .addFilePaths("file5")
                                            .addFilePaths("file6")
                                            .build())
                                    .build())
                            .build())
                    .build())
            .build();

    @Test
    public void testExpandParallelInputs() {
        StepInstance stepInstance = baseStepInstance.toBuilder()
                .setConfiguration(StepConfiguration.newBuilder()
                        .addInputLinks(StepConfiguration.InputLink.newBuilder()
                                .setIdentifier("first-input")
                                .setParallel(true)
                                .build())
                        .build())
                .build();

        List<StepInstance> stepInstances = JobGraph.expandStepInstance(stepInstance);

        assertThat(stepInstances).hasSize(5);
        assertThat(stepInstances.get(0)).isEqualTo(stepInstance.toBuilder()
                .setIdentifier("test-step-1")
                .setParentIdentifier(stepInstance.getIdentifier())
                .setInputs(0, StepDataSet.newBuilder()
                        .setIdentifier("first-input")
                        .setStepInput(StepInput.newBuilder()
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("source-step")
                                        .setIdentifier("first-source")
                                        .setStepOutput(StepOutput.newBuilder().addFilePaths("file1").build())
                                        .build())
                                .build())
                        .build())
                .build());
        assertThat(stepInstances.get(1)).isEqualTo(stepInstance.toBuilder()
                .setIdentifier("test-step-2")
                .setParentIdentifier(stepInstance.getIdentifier())
                .setInputs(0, StepDataSet.newBuilder()
                        .setIdentifier("first-input")
                        .setStepInput(StepInput.newBuilder()
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("source-step")
                                        .setIdentifier("first-source")
                                        .setStepOutput(StepOutput.newBuilder().addFilePaths("file2").build())
                                        .build())
                                .build())
                        .build())
                .build());
        assertThat(stepInstances.get(2)).isEqualTo(stepInstance.toBuilder()
                .setIdentifier("test-step-3")
                .setParentIdentifier(stepInstance.getIdentifier())
                .setInputs(0, StepDataSet.newBuilder()
                        .setIdentifier("first-input")
                        .setStepInput(StepInput.newBuilder()
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("source-step")
                                        .setIdentifier("second-source")
                                        .setStepOutput(StepOutput.newBuilder().addFilePaths("file1").build())
                                        .build())
                                .build())
                        .build())
                .build());
        assertThat(stepInstances.get(3)).isEqualTo(stepInstance.toBuilder()
                .setIdentifier("test-step-4")
                .setParentIdentifier(stepInstance.getIdentifier())
                .setInputs(0, StepDataSet.newBuilder()
                        .setIdentifier("first-input")
                        .setStepInput(StepInput.newBuilder()
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("source-step")
                                        .setIdentifier("second-source")
                                        .setStepOutput(StepOutput.newBuilder().addFilePaths("file2").build())
                                        .build())
                                .build())
                        .build())
                .build());
        assertThat(stepInstances.get(4)).isEqualTo(stepInstance.toBuilder()
                .setIdentifier("test-step-5")
                .setParentIdentifier(stepInstance.getIdentifier())
                .setInputs(0, StepDataSet.newBuilder()
                        .setIdentifier("first-input")
                        .setStepInput(StepInput.newBuilder()
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("source-step")
                                        .setIdentifier("second-source")
                                        .setStepOutput(StepOutput.newBuilder().addFilePaths("file3").build())
                                        .build())
                                .build())
                        .build())
                .build());
    }

    @Test
    public void testExpandParallelInputsIncludes() {
        StepInstance stepInstance = baseStepInstance.toBuilder()
                .setConfiguration(StepConfiguration.newBuilder()
                        .addInputLinks(StepConfiguration.InputLink.newBuilder()
                                .setIdentifier("first-input")
                                .setParallel(true)
                                .addIncludes("file1")
                                .build())
                        .build())
                .build();

        List<StepInstance> stepInstances = JobGraph.expandStepInstance(stepInstance);

        assertThat(stepInstances).hasSize(2);
        assertThat(stepInstances.get(0)).isEqualTo(stepInstance.toBuilder()
                .setIdentifier("test-step-1")
                .setParentIdentifier(stepInstance.getIdentifier())
                .setInputs(0, StepDataSet.newBuilder()
                        .setIdentifier("first-input")
                        .setStepInput(StepInput.newBuilder()
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("source-step")
                                        .setIdentifier("first-source")
                                        .setStepOutput(StepOutput.newBuilder().addFilePaths("file1").build())
                                        .build())
                                .build())
                        .build())
                .build());
        assertThat(stepInstances.get(1)).isEqualTo(stepInstance.toBuilder()
                .setIdentifier("test-step-2")
                .setParentIdentifier(stepInstance.getIdentifier())
                .setInputs(0, StepDataSet.newBuilder()
                        .setIdentifier("first-input")
                        .setStepInput(StepInput.newBuilder()
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("source-step")
                                        .setIdentifier("second-source")
                                        .setStepOutput(StepOutput.newBuilder().addFilePaths("file1").build())
                                        .build())
                                .build())
                        .build())
                .build());
    }

    @Test
    public void testExpandParallelInputsExcludes() {
        StepInstance stepInstance = baseStepInstance.toBuilder()
                .setConfiguration(StepConfiguration.newBuilder()
                        .addInputLinks(StepConfiguration.InputLink.newBuilder()
                                .setIdentifier("first-input")
                                .setParallel(true)
                                .addExcludes("file2")
                                .build())
                        .build())
                .build();

        List<StepInstance> stepInstances = JobGraph.expandStepInstance(stepInstance);

        assertThat(stepInstances).hasSize(3);
        assertThat(stepInstances.get(0)).isEqualTo(stepInstance.toBuilder()
                .setIdentifier("test-step-1")
                .setParentIdentifier(stepInstance.getIdentifier())
                .setInputs(0, StepDataSet.newBuilder()
                        .setIdentifier("first-input")
                        .setStepInput(StepInput.newBuilder()
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("source-step")
                                        .setIdentifier("first-source")
                                        .setStepOutput(StepOutput.newBuilder().addFilePaths("file1").build())
                                        .build())
                                .build())
                        .build())
                .build());
        assertThat(stepInstances.get(1)).isEqualTo(stepInstance.toBuilder()
                .setIdentifier("test-step-2")
                .setParentIdentifier(stepInstance.getIdentifier())
                .setInputs(0, StepDataSet.newBuilder()
                        .setIdentifier("first-input")
                        .setStepInput(StepInput.newBuilder()
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("source-step")
                                        .setIdentifier("second-source")
                                        .setStepOutput(StepOutput.newBuilder().addFilePaths("file1").build())
                                        .build())
                                .build())
                        .build())
                .build());
        assertThat(stepInstances.get(2)).isEqualTo(stepInstance.toBuilder()
                .setIdentifier("test-step-3")
                .setParentIdentifier(stepInstance.getIdentifier())
                .setInputs(0, StepDataSet.newBuilder()
                        .setIdentifier("first-input")
                        .setStepInput(StepInput.newBuilder()
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("source-step")
                                        .setIdentifier("second-source")
                                        .setStepOutput(StepOutput.newBuilder().addFilePaths("file3").build())
                                        .build())
                                .build())
                        .build())
                .build());
    }

    @Test
    public void testExpandParallelInputsIncludesAndExcludes() {
        StepInstance stepInstance = baseStepInstance.toBuilder()
                .setConfiguration(StepConfiguration.newBuilder()
                        .addInputLinks(StepConfiguration.InputLink.newBuilder()
                                .setIdentifier("first-input")
                                .setParallel(true)
                                .addIncludes("file?")
                                .addExcludes("file2")
                                .build())
                        .build())
                .build();

        List<StepInstance> stepInstances = JobGraph.expandStepInstance(stepInstance);

        assertThat(stepInstances).hasSize(3);
        assertThat(stepInstances.get(0)).isEqualTo(stepInstance.toBuilder()
                .setIdentifier("test-step-1")
                .setParentIdentifier(stepInstance.getIdentifier())
                .setInputs(0, StepDataSet.newBuilder()
                        .setIdentifier("first-input")
                        .setStepInput(StepInput.newBuilder()
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("source-step")
                                        .setIdentifier("first-source")
                                        .setStepOutput(StepOutput.newBuilder().addFilePaths("file1").build())
                                        .build())
                                .build())
                        .build())
                .build());
        assertThat(stepInstances.get(1)).isEqualTo(stepInstance.toBuilder()
                .setIdentifier("test-step-2")
                .setParentIdentifier(stepInstance.getIdentifier())
                .setInputs(0, StepDataSet.newBuilder()
                        .setIdentifier("first-input")
                        .setStepInput(StepInput.newBuilder()
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("source-step")
                                        .setIdentifier("second-source")
                                        .setStepOutput(StepOutput.newBuilder().addFilePaths("file1").build())
                                        .build())
                                .build())
                        .build())
                .build());
        assertThat(stepInstances.get(2)).isEqualTo(stepInstance.toBuilder()
                .setIdentifier("test-step-3")
                .setParentIdentifier(stepInstance.getIdentifier())
                .setInputs(0, StepDataSet.newBuilder()
                        .setIdentifier("first-input")
                        .setStepInput(StepInput.newBuilder()
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("source-step")
                                        .setIdentifier("second-source")
                                        .setStepOutput(StepOutput.newBuilder().addFilePaths("file3").build())
                                        .build())
                                .build())
                        .build())
                .build());
    }

    @Test
    public void testExpandParallelInputsGroupBy() {
        StepInstance stepInstance = baseStepInstance.toBuilder()
                .setConfiguration(StepConfiguration.newBuilder()
                        .addInputLinks(StepConfiguration.InputLink.newBuilder()
                                .setIdentifier("first-input")
                                .setParallel(true)
                                .setGroupBy("file(\\d)")
                                .build())
                        .build())
                .build();

        List<StepInstance> stepInstances = JobGraph.expandStepInstance(stepInstance);

        assertThat(stepInstances).hasSize(3);
        assertThat(stepInstances.get(0)).isEqualTo(stepInstance.toBuilder()
                .setIdentifier("test-step-1")
                .setParentIdentifier(stepInstance.getIdentifier())
                .setInputs(0, StepDataSet.newBuilder()
                        .setIdentifier("first-input")
                        .setStepInput(StepInput.newBuilder()
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("source-step")
                                        .setIdentifier("first-source")
                                        .setStepOutput(StepOutput.newBuilder().addFilePaths("file1").build())
                                        .build())
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("source-step")
                                        .setIdentifier("second-source")
                                        .setStepOutput(StepOutput.newBuilder().addFilePaths("file1").build())
                                        .build())
                                .build())
                        .build())
                .build());
        assertThat(stepInstances.get(1)).isEqualTo(stepInstance.toBuilder()
                .setIdentifier("test-step-2")
                .setParentIdentifier(stepInstance.getIdentifier())
                .setInputs(0, StepDataSet.newBuilder()
                        .setIdentifier("first-input")
                        .setStepInput(StepInput.newBuilder()
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("source-step")
                                        .setIdentifier("first-source")
                                        .setStepOutput(StepOutput.newBuilder().addFilePaths("file2").build())
                                        .build())
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("source-step")
                                        .setIdentifier("second-source")
                                        .setStepOutput(StepOutput.newBuilder().addFilePaths("file2").build())
                                        .build())
                                .build())
                        .build())
                .build());
        assertThat(stepInstances.get(2)).isEqualTo(stepInstance.toBuilder()
                .setIdentifier("test-step-3")
                .setParentIdentifier(stepInstance.getIdentifier())
                .setInputs(0, StepDataSet.newBuilder()
                        .setIdentifier("first-input")
                        .setStepInput(StepInput.newBuilder()
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("source-step")
                                        .setIdentifier("second-source")
                                        .setStepOutput(StepOutput.newBuilder().addFilePaths("file3").build())
                                        .build())
                                .build())
                        .build())
                .build());
    }

    @Test
    public void testExpandParallelParameters() {
        StepInstance stepInstance = baseStepInstance.toBuilder()
                .setConfiguration(StepConfiguration.newBuilder()
                        .addParameterLinks(StepConfiguration.ParameterLink.newBuilder()
                                .setIdentifier("first-param")
                                .setParallel(true)
                                .build())
                        .build())
                .build();

        List<StepInstance> stepInstances = JobGraph.expandStepInstance(stepInstance);

        assertThat(stepInstances).hasSize(3);
        assertThat(stepInstances.get(0)).isEqualTo(stepInstance.toBuilder()
                .setIdentifier("test-step-1")
                .setParentIdentifier(stepInstance.getIdentifier())
                .setParameters(0, StepParameterValue.newBuilder()
                        .setIdentifier("first-param")
                        .addValues("param-value1")
                        .build())
                .build());
        assertThat(stepInstances.get(1)).isEqualTo(stepInstance.toBuilder()
                .setIdentifier("test-step-2")
                .setParentIdentifier(stepInstance.getIdentifier())
                .setParameters(0, StepParameterValue.newBuilder()
                        .setIdentifier("first-param")
                        .addValues("param-value2")
                        .build())
                .build());
        assertThat(stepInstances.get(2)).isEqualTo(stepInstance.toBuilder()
                .setIdentifier("test-step-3")
                .setParentIdentifier(stepInstance.getIdentifier())
                .setParameters(0, StepParameterValue.newBuilder()
                        .setIdentifier("first-param")
                        .addValues("param-value3")
                        .build())
                .build());
    }


    @Test
    public void testExpandStepInstanceCombined() {
        // Multiple parallel=true flags results in a matrix configuration
        StepInstance stepInstance = baseStepInstance.toBuilder()
                .setConfiguration(StepConfiguration.newBuilder()
                        .addParameterLinks(StepConfiguration.ParameterLink.newBuilder()
                                .setIdentifier("first-param")
                                .setParallel(true)
                                .build())
                        .addInputLinks(StepConfiguration.InputLink.newBuilder()
                                .setIdentifier("first-input")
                                .setParallel(true)
                                .addExcludes("file2")
                                .setGroupBy("file(\\d)")
                                .build())
                        .build())
                .build();

        List<StepInstance> stepInstances = JobGraph.expandStepInstance(stepInstance);

        assertThat(stepInstances).hasSize(6); // three parallel parameter values, two grouped input values
        assertThat(stepInstances.get(0)).isEqualTo(stepInstance.toBuilder()
                .setIdentifier("test-step-1")
                .setParentIdentifier(stepInstance.getIdentifier())
                .setParameters(0, StepParameterValue.newBuilder()
                        .setIdentifier("first-param")
                        .addValues("param-value1")
                        .build())
                .setInputs(0, StepDataSet.newBuilder()
                        .setIdentifier("first-input")
                        .setStepInput(StepInput.newBuilder()
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("source-step")
                                        .setIdentifier("first-source")
                                        .setStepOutput(StepOutput.newBuilder().addFilePaths("file1").build())
                                        .build())
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("source-step")
                                        .setIdentifier("second-source")
                                        .setStepOutput(StepOutput.newBuilder().addFilePaths("file1").build())
                                        .build())
                                .build())
                        .build())
                .build());
        assertThat(stepInstances.get(1)).isEqualTo(stepInstance.toBuilder()
                .setIdentifier("test-step-2")
                .setParentIdentifier(stepInstance.getIdentifier())
                .setParameters(0, StepParameterValue.newBuilder()
                        .setIdentifier("first-param")
                        .addValues("param-value2")
                        .build())
                .setInputs(0, StepDataSet.newBuilder()
                        .setIdentifier("first-input")
                        .setStepInput(StepInput.newBuilder()
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("source-step")
                                        .setIdentifier("first-source")
                                        .setStepOutput(StepOutput.newBuilder().addFilePaths("file1").build())
                                        .build())
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("source-step")
                                        .setIdentifier("second-source")
                                        .setStepOutput(StepOutput.newBuilder().addFilePaths("file1").build())
                                        .build())
                                .build())
                        .build())
                .build());
        assertThat(stepInstances.get(2)).isEqualTo(stepInstance.toBuilder()
                .setIdentifier("test-step-3")
                .setParentIdentifier(stepInstance.getIdentifier())
                .setParameters(0, StepParameterValue.newBuilder()
                        .setIdentifier("first-param")
                        .addValues("param-value3")
                        .build())
                .setInputs(0, StepDataSet.newBuilder()
                        .setIdentifier("first-input")
                        .setStepInput(StepInput.newBuilder()
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("source-step")
                                        .setIdentifier("first-source")
                                        .setStepOutput(StepOutput.newBuilder().addFilePaths("file1").build())
                                        .build())
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("source-step")
                                        .setIdentifier("second-source")
                                        .setStepOutput(StepOutput.newBuilder().addFilePaths("file1").build())
                                        .build())
                                .build())
                        .build())
                .build());
        assertThat(stepInstances.get(3)).isEqualTo(stepInstance.toBuilder()
                .setIdentifier("test-step-4")
                .setParentIdentifier(stepInstance.getIdentifier())
                .setParameters(0, StepParameterValue.newBuilder()
                        .setIdentifier("first-param")
                        .addValues("param-value1")
                        .build())
                .setInputs(0, StepDataSet.newBuilder()
                        .setIdentifier("first-input")
                        .setStepInput(StepInput.newBuilder()
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("source-step")
                                        .setIdentifier("second-source")
                                        .setStepOutput(StepOutput.newBuilder().addFilePaths("file3").build())
                                        .build())
                                .build())
                        .build())
                .build());
        assertThat(stepInstances.get(4)).isEqualTo(stepInstance.toBuilder()
                .setIdentifier("test-step-5")
                .setParentIdentifier(stepInstance.getIdentifier())
                .setParameters(0, StepParameterValue.newBuilder()
                        .setIdentifier("first-param")
                        .addValues("param-value2")
                        .build())
                .setInputs(0, StepDataSet.newBuilder()
                        .setIdentifier("first-input")
                        .setStepInput(StepInput.newBuilder()
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("source-step")
                                        .setIdentifier("second-source")
                                        .setStepOutput(StepOutput.newBuilder().addFilePaths("file3").build())
                                        .build())
                                .build())
                        .build())
                .build());
        assertThat(stepInstances.get(5)).isEqualTo(stepInstance.toBuilder()
                .setIdentifier("test-step-6")
                .setParentIdentifier(stepInstance.getIdentifier())
                .setParameters(0, StepParameterValue.newBuilder()
                        .setIdentifier("first-param")
                        .addValues("param-value3")
                        .build())
                .setInputs(0, StepDataSet.newBuilder()
                        .setIdentifier("first-input")
                        .setStepInput(StepInput.newBuilder()
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("source-step")
                                        .setIdentifier("second-source")
                                        .setStepOutput(StepOutput.newBuilder().addFilePaths("file3").build())
                                        .build())
                                .build())
                        .build())
                .build());
    }

    @Test
    public void testExpandParallelSingle() {
        StepInstance stepInstance = baseStepInstance.toBuilder()
                .setConfiguration(StepConfiguration.newBuilder()
                        .addInputLinks(StepConfiguration.InputLink.newBuilder()
                                .setIdentifier("first-input")
                                .setParallel(true)
                                .build())
                        .build())
                .setInputs(0, baseStepInstance.getInputs(0).toBuilder()
                        .setStepInput(StepInput.newBuilder()
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("source-step")
                                        .setIdentifier("first-source")
                                        .setStepOutput(StepOutput.newBuilder()
                                                .addFilePaths("file1")
                                                .build())
                                        .build())
                                .build())
                        .build())
                .build();

        List<StepInstance> stepInstances = JobGraph.expandStepInstance(stepInstance);

        assertThat(stepInstances).hasSize(1);
        assertThat(stepInstances.get(0)).isEqualTo(stepInstance.toBuilder()
                .setIdentifier("test-step-1")
                .setParentIdentifier(stepInstance.getIdentifier())
                .setInputs(0, StepDataSet.newBuilder()
                        .setIdentifier("first-input")
                        .setStepInput(StepInput.newBuilder()
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("source-step")
                                        .setIdentifier("first-source")
                                        .setStepOutput(StepOutput.newBuilder().addFilePaths("file1").build())
                                        .build())
                                .build())
                        .build())
                .build());
    }

}