package com.cgi.eoss.eopp.workflow;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.util.JsonFormat;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.Reader;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

/**
 * <p>Utility methods for working with {@link Workflow} objects.</p>
 */
public final class Workflows {

    private Workflows() {
        // no-op for utility class
    }

    /**
     * <p>Deserialise a {@link Workflow} instance from its YAML representation.</p>
     *
     * @param yamlReader Character stream source containing the YAML Workflow.
     * @return The {@link Workflow} instance parsed from the Reader.
     */
    public static Workflow workflowFromYaml(Reader yamlReader) throws IOException {
        return workflowFromYaml(yamlReader, Collections.emptyList());
    }

    /**
     * <p>Deserialise a {@link Workflow} instance from its YAML representation.</p>
     * <p>Some YAML Workflow specs may not match the proto exactly. In-line conversion of the YAML-parsed document is
     * possible, according to the given {@link YamlTransformer}s.</p>
     * <p>If no transformers are supplied, we assume a clean mapping to the proto.</p>
     *
     * @param yamlReader       Character stream source containing the YAML Workflow.
     * @param yamlTransformers ObjectNode transformations to be applied to the YAML Workflow before reading it as a
     *                         {@link Workflow}.
     * @return The {@link Workflow} instance parsed from the Reader.
     */
    public static Workflow workflowFromYaml(Reader yamlReader, List<YamlTransformer> yamlTransformers) throws IOException {
        Workflow.Builder builder = Workflow.newBuilder();
        readYamlToProtobuf(yamlReader, yamlTransformers, builder);
        return builder.build();
    }

    /**
     * <p>Deserialise a {@link Step} instance from its YAML representation.</p>
     *
     * @param yamlReader Character stream source containing the YAML Step.
     * @return The {@link Step} instance parsed from the Reader.
     */
    public static Step stepFromYaml(Reader yamlReader) throws IOException {
        return stepFromYaml(yamlReader, Collections.emptyList());
    }

    /**
     * <p>Deserialise a {@link Step} instance from its YAML representation.</p>
     * <p>Some YAML Step specs may not match the proto exactly. In-line conversion of the YAML-parsed document is
     * possible, according to the given {@link YamlTransformer}s.</p>
     * <p>If no transformers are supplied, we assume a clean mapping to the proto.</p>
     *
     * @param yamlReader       Character stream source containing the YAML Step.
     * @param yamlTransformers ObjectNode transformations to be applied to the YAML Step before reading it as a
     *                         {@link Step}.
     * @return The {@link Step} instance parsed from the Reader.
     */
    public static Step stepFromYaml(Reader yamlReader, List<YamlTransformer> yamlTransformers) throws IOException {
        Step.Builder builder = Step.newBuilder();
        readYamlToProtobuf(yamlReader, yamlTransformers, builder);
        return builder.build();
    }

    private static <T extends GeneratedMessageV3.Builder<T>> void readYamlToProtobuf(Reader yamlReader, List<YamlTransformer> yamlTransformers, T builder) throws IOException {
        // JSON -> Protobuf is easily achieved by the provided JsonFormat, so convert the YAML into JSON and use that.
        ObjectNode yamlObject;
        try (YAMLParser yamlParser = new YAMLMapper().getFactory().createParser(yamlReader)) {
            // Read the YAML to a generic node object
            yamlObject = yamlParser.readValueAsTree();
        }

        // Transform the YAML according to any supplied transformers
        yamlTransformers.forEach(it -> it.transform(yamlObject));

        // Link the Writer (for Jackson to serialise JSON) and the Reader (for JsonFormat.parser() to deserialise)
        try (PipedWriter pipedWriter = new PipedWriter(); PipedReader protobufReader = new PipedReader(pipedWriter)) {
            // Do the write in a new thread to avoid blocking on the pipe
            Executors.newSingleThreadExecutor().submit((Callable<Void>) () -> {
                new JsonMapper().getFactory().createGenerator(pipedWriter).writeTree(yamlObject);
                return null;
            });
            JsonFormat.parser().merge(protobufReader, builder);
        }
    }

    /**
     * <p>A customiser for a Jackson {@link ObjectNode}, for example parsed from a Workflow or Step YAML representation.</p>
     * <p>A set of transformations can be applied to bring an application-specific model of a {@link Workflow} or
     * {@link Step} in line with the protobuf representation.</p>
     */
    @FunctionalInterface
    public interface YamlTransformer {
        /**
         * <p>Manipulate in-place the given {@link ObjectNode}.</p>
         */
        void transform(ObjectNode yamlObject);
    }
}
