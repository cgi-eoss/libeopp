package com.cgi.eoss.eopp.jobgraph;

import com.cgi.eoss.eopp.identifier.Identifier;
import com.cgi.eoss.eopp.identifier.Identifiers;
import com.cgi.eoss.eopp.job.StepDataSet;
import com.cgi.eoss.eopp.job.StepInput;
import com.cgi.eoss.eopp.job.StepInstance;
import com.cgi.eoss.eopp.job.StepParameterValue;
import com.cgi.eoss.eopp.workflow.DataSources;
import com.cgi.eoss.eopp.workflow.Input;
import com.cgi.eoss.eopp.workflow.Output;
import com.cgi.eoss.eopp.workflow.Parameter;
import com.cgi.eoss.eopp.workflow.Step;
import com.cgi.eoss.eopp.workflow.StepConfiguration;
import com.cgi.eoss.eopp.workflow.Workflow;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.graph.Network;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.util.Arrays;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.extensions.proto.ProtoTruth.assertThat;
import static org.junit.Assert.fail;

public class JobGraphTest {

    private Workflow workflow;

    @Before
    public void setUp() throws Exception {
        // A fairly comprehensive workflow to exercise the various node/edge conditions
        Step first = Step.newBuilder()
                .setIdentifier(Identifier.newBuilder().setIdentifier("first").setVersion("1.0.0").build())
                .addParameters(Parameter.newBuilder().setIdentifier("SOURCED_PARAM").setMinOccurs(1).build())
                .addInputs(Input.newBuilder().setIdentifier("in1").setMinOccurs(1).build())
                .addOutputs(Output.newBuilder().setIdentifier("out1").setMinOccurs(1).build())
                .build();
        Step trimmed = Step.newBuilder()
                .setIdentifier(Identifier.newBuilder().setIdentifier("trimmed").setVersion("1.0.0").build())
                .addParameters(Parameter.newBuilder().setIdentifier("UNPROVIDED_PARAM").setMinOccurs(0).build())
                .addInputs(Input.newBuilder().setIdentifier("in1").setMinOccurs(1).build())
                .addOutputs(Output.newBuilder().setIdentifier("out1").setMinOccurs(1).build())
                .build();
        Step second = Step.newBuilder()
                .setIdentifier(Identifier.newBuilder().setIdentifier("second").setVersion("1.0.0").build())
                .addParameters(Parameter.newBuilder().setIdentifier("DEFAULTED_PARAM").setMinOccurs(1).addDefaultValues("default").build())
                .addParameters(Parameter.newBuilder().setIdentifier("HARDCODED_PARAM").setMinOccurs(1).build())
                .addInputs(Input.newBuilder().setIdentifier("in1").setMinOccurs(1).build())
                .addOutputs(Output.newBuilder().setIdentifier("out1").setMinOccurs(1).build())
                .build();
        Step skipped = Step.newBuilder()
                .setIdentifier(Identifier.newBuilder().setIdentifier("skipped").setVersion("1.0.0").build())
                .addInputs(Input.newBuilder().setIdentifier("in1").setMinOccurs(2).build())
                .addOutputs(Output.newBuilder().setIdentifier("out1").setMinOccurs(1).build())
                .build();

        workflow = Workflow.newBuilder()
                .addParameters(Parameter.newBuilder().setIdentifier("sourced_param").setMinOccurs(0).build())
                .addInputs(Input.newBuilder().setIdentifier("provided_workflow_input").setMinOccurs(1).build())
                .addInputs(Input.newBuilder().setIdentifier("unprovided_workflow_input").setMinOccurs(1).build())
                .addOutputs(Output.newBuilder().setIdentifier("workflow_output").setMinOccurs(1)
                        .setSources(DataSources.newBuilder()
                                .addStepOutputs(DataSources.StepOutput.newBuilder().setStepIdentifier("second-step").setOutputIdentifier("out1").build())
                                .addStepOutputs(DataSources.StepOutput.newBuilder().setStepIdentifier("no-param-successor-step").setOutputIdentifier("out1").build())
                                .addStepOutputs(DataSources.StepOutput.newBuilder().setStepIdentifier("skipped-step").setOutputIdentifier("out1").build())
                                .build())
                        .build())
                .addStepConfigurations(StepConfiguration.newBuilder()
                        .setIdentifier("first-step")
                        .setStep(first)
                        .addParameterLinks(StepConfiguration.ParameterLink.newBuilder()
                                .setIdentifier("SOURCED_PARAM")
                                .setWorkflowParameter("sourced_param").build()
                        )
                        .addInputLinks(StepConfiguration.InputLink.newBuilder()
                                .setIdentifier("in1")
                                .setSources(DataSources.newBuilder()
                                        .addWorkflowInputs("provided_workflow_input").build()).build()
                        )
                        .build())
                .addStepConfigurations(StepConfiguration.newBuilder()
                        .setIdentifier("no-param-step")
                        .setStep(trimmed)
                        .addParameterLinks(StepConfiguration.ParameterLink.newBuilder()
                                .setIdentifier("UNPROVIDED_PARAM")
                                .setWorkflowParameter("unprovided_param")
                                .setSkipStepIfEmpty(true).build()
                        )
                        .addInputLinks(StepConfiguration.InputLink.newBuilder()
                                .setIdentifier("in1")
                                .setSources(DataSources.newBuilder()
                                        .addWorkflowInputs("provided_workflow_input").build()).build()
                        )
                        .build())
                .addStepConfigurations(StepConfiguration.newBuilder()
                        .setIdentifier("no-param-successor-step")
                        .setStep(trimmed)
                        .addInputLinks(StepConfiguration.InputLink.newBuilder()
                                .setIdentifier("in1")
                                .setSources(DataSources.newBuilder()
                                        .addStepOutputs(DataSources.StepOutput.newBuilder()
                                                .setStepIdentifier("no-param-step")
                                                .setOutputIdentifier("out1").build()).build()).build()
                        )
                        .build())
                .addStepConfigurations(StepConfiguration.newBuilder()
                        .setIdentifier("trimmed-step")
                        .setStep(trimmed)
                        .addInputLinks(StepConfiguration.InputLink.newBuilder()
                                .setIdentifier("in1")
                                .setSources(DataSources.newBuilder()
                                        .addWorkflowInputs("unprovided_workflow_input").build()).build()
                        )
                        .build())
                .addStepConfigurations(StepConfiguration.newBuilder()
                        .setIdentifier("second-step")
                        .setStep(second)
                        .addParameterLinks(StepConfiguration.ParameterLink.newBuilder()
                                .setIdentifier("HARDCODED_PARAM")
                                .addAllHardcodedValues(ImmutableList.of("a", "list", "of", "strings"))
                                .build()
                        )
                        .addInputLinks(StepConfiguration.InputLink.newBuilder()
                                .setIdentifier("in1")
                                .setSources(DataSources.newBuilder()
                                        .addStepOutputs(DataSources.StepOutput.newBuilder()
                                                .setStepIdentifier("first-step")
                                                .setOutputIdentifier("out1").build()).build()).build()
                        )
                        .build())
                .addStepConfigurations(StepConfiguration.newBuilder()
                        .setIdentifier("skipped-step")
                        .setStep(skipped)
                        .addInputLinks(StepConfiguration.InputLink.newBuilder()
                                .setIdentifier("in1")
                                .setSources(DataSources.newBuilder()
                                        .addStepOutputs(DataSources.StepOutput.newBuilder()
                                                .setStepIdentifier("first-step")
                                                .setOutputIdentifier("out1").build()).build()).build()
                        )
                        .build())
                .build();
    }

    @Test
    public void testBuildWithSkippedSteps() {
        JobGraph jobGraph = JobGraph.builder("test-job-id")
                .withWorkflow(workflow)
                .withInput("provided_workflow_input", URI.create("file:///etc/hosts"))
                .withParameter("sourced_param", "1")
                .build();

        Network<com.cgi.eoss.eopp.jobgraph.Step, DataConnector> network = jobGraph.getNetwork();
        assertThat(network.nodes()).hasSize(5);

        ImmutableMap<String, StepInstance> steps = Maps.uniqueIndex(jobGraph.getSteps(), StepInstance::getIdentifier);

        assertThat(steps.get("INPUT-provided_workflow_input")).isEqualTo(StepInstance.newBuilder().setIdentifier("INPUT-provided_workflow_input")
                .setType(StepInstance.Type.INPUT)
                .addOutputs(StepDataSet.newBuilder()
                        .setStepIdentifier("INPUT-provided_workflow_input")
                        .setIdentifier("provided_workflow_input")
                        .build()).build());

        assertThat(steps.get("first-step")).isEqualTo(StepInstance.newBuilder().setIdentifier("first-step")
                .setStepIdentifier(Identifiers.parse("first:1.0.0"))
                .setType(StepInstance.Type.PROCESS)
                .addParameters(StepParameterValue.newBuilder().setIdentifier("SOURCED_PARAM").addValues("1").build())
                .addInputs(StepDataSet.newBuilder()
                        .setStepIdentifier("first-step")
                        .setIdentifier("in1")
                        .setStepInput(StepInput.newBuilder()
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("INPUT-provided_workflow_input")
                                        .setIdentifier("provided_workflow_input")
                                        .build()).build()).build())
                .addOutputs(StepDataSet.newBuilder()
                        .setStepIdentifier("first-step")
                        .setIdentifier("out1")
                        .build()).build());

        assertThat(steps.get("second-step")).isEqualTo(StepInstance.newBuilder().setIdentifier("second-step")
                .setStepIdentifier(Identifiers.parse("second:1.0.0"))
                .setType(StepInstance.Type.PROCESS)
                .addParameters(StepParameterValue.newBuilder().setIdentifier("DEFAULTED_PARAM").addValues("default").build())
                .addParameters(StepParameterValue.newBuilder().setIdentifier("HARDCODED_PARAM").addAllValues(Arrays.asList("a", "list", "of", "strings")).build())
                .addInputs(StepDataSet.newBuilder()
                        .setStepIdentifier("second-step")
                        .setIdentifier("in1")
                        .setStepInput(StepInput.newBuilder()
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("first-step")
                                        .setIdentifier("out1")
                                        .build()).build()).build())
                .addOutputs(StepDataSet.newBuilder()
                        .setStepIdentifier("second-step")
                        .setIdentifier("out1")
                        .build()).build());

        assertThat(steps.get("OUTPUT-workflow_output")).isEqualTo(StepInstance.newBuilder().setIdentifier("OUTPUT-workflow_output")
                .setType(StepInstance.Type.OUTPUT)
                .addInputs(StepDataSet.newBuilder()
                        .setStepIdentifier("OUTPUT-workflow_output")
                        .setIdentifier("workflow_output")
                        .setStepInput(StepInput.newBuilder()
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("skipped-step")
                                        .setIdentifier("out1").build())
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("second-step")
                                        .setIdentifier("out1").build())
                                .build()).build()).build());

        assertThat(steps.containsKey("INPUT-unprovided_workflow_input")).isFalse(); // no value was supplied to this input
        assertThat(steps.containsKey("trimmed-step")).isFalse(); // no steps consume this step's output
    }

    @Test
    public void buildStepsWithUnskippedEmptyParameters() {
        // Set no-param-step's parameter.skipStepIfEmpty to false - adding a branch of two steps to the graph
        Workflow lenientWorkflow = workflow.toBuilder()
                .setStepConfigurations(1, workflow.getStepConfigurations(1).toBuilder()
                        .setParameterLinks(0, workflow.getStepConfigurations(1)
                                .getParameterLinks(0).toBuilder().setSkipStepIfEmpty(false)))
                .build();

        JobGraph jobGraph = JobGraph.builder("test-job-id")
                .withWorkflow(lenientWorkflow)
                .withInput("provided_workflow_input", URI.create("file:///etc/hosts"))
                .withParameter("sourced_param", "1")
                .build();

        Network<com.cgi.eoss.eopp.jobgraph.Step, DataConnector> network = jobGraph.getNetwork();
        assertThat(network.nodes()).hasSize(7);

        ImmutableMap<String, StepInstance> steps = Maps.uniqueIndex(jobGraph.getSteps(), StepInstance::getIdentifier);

        assertThat(steps.get("no-param-step")).isEqualTo(StepInstance.newBuilder().setIdentifier("no-param-step")
                .setStepIdentifier(Identifiers.parse("trimmed:1.0.0"))
                .setType(StepInstance.Type.PROCESS)
                .addParameters(StepParameterValue.newBuilder().setIdentifier("UNPROVIDED_PARAM").build()) // no values!
                .addInputs(StepDataSet.newBuilder()
                        .setStepIdentifier("no-param-step")
                        .setIdentifier("in1")
                        .setStepInput(StepInput.newBuilder()
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("INPUT-provided_workflow_input")
                                        .setIdentifier("provided_workflow_input")
                                        .build()).build()).build())
                .addOutputs(StepDataSet.newBuilder()
                        .setStepIdentifier("no-param-step")
                        .setIdentifier("out1")
                        .build()).build());

        assertThat(steps.get("no-param-successor-step")).isEqualTo(StepInstance.newBuilder().setIdentifier("no-param-successor-step")
                .setStepIdentifier(Identifiers.parse("trimmed:1.0.0"))
                .setType(StepInstance.Type.PROCESS)
                .addParameters(StepParameterValue.newBuilder().setIdentifier("UNPROVIDED_PARAM").build()) // no values!
                .addInputs(StepDataSet.newBuilder()
                        .setStepIdentifier("no-param-successor-step")
                        .setIdentifier("in1")
                        .setStepInput(StepInput.newBuilder()
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("no-param-step")
                                        .setIdentifier("out1")
                                        .build()).build()).build())
                .addOutputs(StepDataSet.newBuilder()
                        .setStepIdentifier("no-param-successor-step")
                        .setIdentifier("out1")
                        .build()).build());

        assertThat(steps.get("OUTPUT-workflow_output")).isEqualTo(StepInstance.newBuilder().setIdentifier("OUTPUT-workflow_output")
                .setType(StepInstance.Type.OUTPUT)
                .addInputs(StepDataSet.newBuilder()
                        .setStepIdentifier("OUTPUT-workflow_output")
                        .setIdentifier("workflow_output")
                        .setStepInput(StepInput.newBuilder()
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("skipped-step")
                                        .setIdentifier("out1").build())
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("second-step")
                                        .setIdentifier("out1").build())
                                .addSources(StepDataSet.newBuilder()
                                        .setStepIdentifier("no-param-successor-step")
                                        .setIdentifier("out1").build())
                                .build()).build()).build());

        assertThat(steps.containsKey("INPUT-unprovided_workflow_input")).isFalse(); // no value was supplied to this input
        assertThat(steps.containsKey("trimmed-step")).isFalse(); // no steps consume this step's output
    }

    @Test
    public void buildEmpty() {
        // A workflow with inputs having minOccurs > 0
        Workflow workflow = Workflow.newBuilder()
                .addInputs(Input.newBuilder().setIdentifier("required_input").setMinOccurs(2).build())
                .addInputs(Input.newBuilder().setIdentifier("optional_input").setMinOccurs(0).build())
                .build();

        // Don't provide all required inputs
        InputStubbing builder = JobGraph.builder("test-job-id")
                .withWorkflow(workflow)
                .withInput("required_input", URI.create("http://example.com/required"))
                .withInput("optional_input", URI.create("http://example.com/optional"));

        try {
            builder.build();
            fail("Expected GraphBuildFailureException");
        } catch (GraphBuildFailureException e) {
            assertThat(e.getMessage()).isEqualTo("Network was empty after illegal source and sink step removal");
        }
    }

}