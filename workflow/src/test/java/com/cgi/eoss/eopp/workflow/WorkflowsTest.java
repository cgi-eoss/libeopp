package com.cgi.eoss.eopp.workflow;

import com.cgi.eoss.eopp.identifier.Identifiers;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.extensions.proto.ProtoTruth.assertThat;

@RunWith(JUnit4.class)
public class WorkflowsTest {

    private static final Workflow TEST_WORKFLOW = Workflow.newBuilder()
            .setIdentifier(Identifiers.parse("test-workflow:1.0.0"))
            .setTitle("Test Workflow")
            .setAbstract("This is a test workflow.\n\nIt has a multiline abstract.")
            .addKeywords("some")
            .addKeywords("test")
            .addKeywords("keywords")
            .addParameters(Parameter.newBuilder()
                    .setIdentifier("param-1")
                    .setTitle("Parameter 1")
                    .setMinOccurs(1)
                    .setMaxOccurs(10)
                    .setDataType(ParameterDataType.STRING)
                    .setUom("candela")
                    .addDefaultValues("param-1-value-1")
                    .addDefaultValues("param-1-value-2")
                    .addAllowedValues(Parameter.AllowedValue.newBuilder().setValue("param-1-value-1").setType(Parameter.AllowedValue.Type.VALUE).build())
                    .addAllowedValues(Parameter.AllowedValue.newBuilder().setValue("param-1-value-2").setType(Parameter.AllowedValue.Type.VALUE).build())
                    .addAllowedValues(Parameter.AllowedValue.newBuilder().setValue("param-1-value-[0-9]+").setType(Parameter.AllowedValue.Type.REGEX).build())
                    .build())
            .addInputs(Input.newBuilder()
                    .setIdentifier("input-1")
                    .setTitle("Input 1")
                    .setAbstract("Some random input data")
                    .setMinOccurs(0)
                    .setFormat("SAFE")
                    .build())
            .addInputs(Input.newBuilder()
                    .setIdentifier("input-2")
                    .setTitle("Input 2")
                    .setAbstract("Some more random input data")
                    .setMinOccurs(1)
                    .setDataType(ParameterDataType.UNKNOWN)
                    .build())
            .addOutputs(Output.newBuilder()
                    .setIdentifier("output-1")
                    .setTitle("Output 1")
                    .setAbstract("A step output file.")
                    .setMinOccurs(1)
                    .setMaxOccurs(1)
                    .setFormat("SAFE")
                    .setSources(DataSources.newBuilder()
                            .addStepOutputs(DataSources.StepOutput.newBuilder()
                                    .setStepIdentifier("processing-step-1")
                                    .setOutputIdentifier("step-output")
                                    .build())
                            .build())
                    .build())
            .addOutputs(Output.newBuilder()
                    .setIdentifier("output-2")
                    .setTitle("Output 2")
                    .setAbstract("A passed-through workflow input.")
                    .setMinOccurs(0)
                    .setFormat("SAFE")
                    .setSources(DataSources.newBuilder()
                            .addWorkflowInputs("input-1")
                            .build())
                    .build())
            .addStepConfigurations(StepConfiguration.newBuilder()
                    .setIdentifier("processing-step-1")
                    .setStep(Step.newBuilder().setIdentifier(Identifiers.parse("test-step:1.0.0")).build())
                    .addParameterLinks(StepConfiguration.ParameterLink.newBuilder()
                            .setIdentifier("test-step-param-1")
                            .addHardcodedValues("hardcoded-test-value")
                            .build())
                    .addParameterLinks(StepConfiguration.ParameterLink.newBuilder()
                            .setIdentifier("test-step-param-2")
                            .setWorkflowParameter("param-1")
                            .build())
                    .addInputLinks(StepConfiguration.InputLink.newBuilder()
                            .setIdentifier("test-step-input-1")
                            .setSources(DataSources.newBuilder()
                                    .addWorkflowInputs("input-2")
                                    .build())
                            .setParallel(true)
                            .build())
                    .build())
            .build();

    private static Step TEST_STEP = Step.newBuilder()
            .setIdentifier(Identifiers.parse("test:2.0.1"))
            .setTitle("Test Step")
            .setAbstract("A test step.")
            .addKeywords("some")
            .addKeywords("key")
            .addKeywords("words")
            .addParameters(Parameter.newBuilder()
                    .setIdentifier("BAZ")
                    .setMinOccurs(1)
                    .setDataType(ParameterDataType.STRING)
                    .addDefaultValues("default")
                    .build())
            .addParameters(Parameter.newBuilder()
                    .setIdentifier("SHOULD_PASS")
                    .setMinOccurs(1)
                    .setMaxOccurs(1)
                    .setDataType(ParameterDataType.BOOLEAN)
                    .addDefaultValues("true")
                    .build())
            .addParameters(Parameter.newBuilder()
                    .setIdentifier("SLEEP_TIME")
                    .setMinOccurs(1)
                    .setMaxOccurs(1)
                    .setDataType(ParameterDataType.INTEGER)
                    .addDefaultValues("2")
                    .setUom("seconds")
                    .build())
            .addInputs(Input.newBuilder()
                    .setIdentifier("test_input")
                    .setTitle("Dummy Input")
                    .setAbstract("Some dummy input file.")
                    .setMinOccurs(1)
                    .setMaxOccurs(1)
                    .build())
            .addOutputs(Output.newBuilder()
                    .setIdentifier("test_output")
                    .setTitle("Test Output")
                    .setAbstract("A dummy output file.")
                    .setMinOccurs(1)
                    .setMaxOccurs(1)
                    .build())
            .setRequests(Step.Requests.newBuilder()
                    .setCpu("50m")
                    .setMemory("50Mi")
                    .build())
            .build();

    @Test
    public void workflowFromYaml() throws IOException {
        Reader reader = new BufferedReader(new InputStreamReader(WorkflowsTest.class.getResourceAsStream("/test-workflow.yml")));
        Workflow workflow = Workflows.workflowFromYaml(reader);
        assertThat(workflow).isEqualTo(TEST_WORKFLOW);
    }

    @Test
    public void workflowFromYamlEmbeddedIdentifier() throws IOException {
        List<Workflows.YamlTransformer> yamlTransformers = ImmutableList.of(
                // Pretend we select further transformers based on this field, which is not part of the Workflow proto
                workflowNode -> {
                    assertThat(workflowNode.get("apiVersion").textValue()).isEqualTo("eopp/v1+embedded.id");
                    workflowNode.remove("apiVersion");
                },
                // Walk the workflow plus its step_configuration[].{step,nested_workflow} nodes to extract embedded identifier fields to the Identifier struct
                workflowNode -> Stream.concat(Stream.of(workflowNode), Optional.ofNullable((ArrayNode) workflowNode.get("step_configurations"))
                        .map(stepConfigurations -> StreamSupport.stream(stepConfigurations.spliterator(), false)
                                .flatMap(stepConfig -> Optional.ofNullable(stepConfig.has("step") ? stepConfig.get("step") : stepConfig.get("nested_workflow"))
                                        .map(Stream::of).orElse(Stream.empty())))
                        .orElse(Stream.empty()))
                        .forEach(it -> {
                            String identifier = it.get("identifier").textValue();
                            String version = it.get("version").textValue();
                            ((ObjectNode) it).remove("identifier");
                            ((ObjectNode) it).remove("version");
                            ObjectNode newIdentifierNode = ((ObjectNode) it).putObject("identifier");
                            newIdentifierNode.put("identifier", identifier);
                            newIdentifierNode.put("version", version);
                        }));

        Reader reader = new BufferedReader(new InputStreamReader(WorkflowsTest.class.getResourceAsStream("/test-workflow-embedded-identifier.yml")));
        Workflow workflow = Workflows.workflowFromYaml(reader, yamlTransformers);
        assertThat(workflow).isEqualTo(TEST_WORKFLOW);
    }

    @Test
    public void stepFromYaml() throws IOException {
        Reader reader = new BufferedReader(new InputStreamReader(WorkflowsTest.class.getResourceAsStream("/test-step.yml")));
        Step step = Workflows.stepFromYaml(reader);
        assertThat(step).isEqualTo(TEST_STEP);
    }

    @Test
    public void stepFromYamlTransformations() throws IOException {
        List<Workflows.YamlTransformer> yamlTransformers = ImmutableList.of(
                // Extract embedded identifier fields to the Identifier struct
                stepNode -> {
                    String identifier = stepNode.get("identifier").textValue();
                    String version = stepNode.get("version").textValue();
                    stepNode.remove("identifier");
                    stepNode.remove("version");
                    ObjectNode newIdentifierNode = stepNode.putObject("identifier");
                    newIdentifierNode.put("identifier", identifier);
                    newIdentifierNode.put("version", version);
                },
                // Transform single-valued default_value to a list, and resolve case-sensitive data_type enum
                stepNode -> {
                    ArrayNode parameters = (ArrayNode) stepNode.get("parameters");
                    parameters.forEach(p -> {
                        ObjectNode paramNode = (ObjectNode) p;
                        paramNode.putArray("default_values").add(paramNode.get("default_value"));
                        paramNode.remove("default_value");
                        paramNode.put("data_type", paramNode.get("data_type").textValue().toUpperCase());
                    });
                }
        );

        Reader reader = new BufferedReader(new InputStreamReader(WorkflowsTest.class.getResourceAsStream("/test-step-enumcase-defaultvalues.yml")));
        Step step = Workflows.stepFromYaml(reader, yamlTransformers);
        assertThat(step).isEqualTo(TEST_STEP);
    }

}
