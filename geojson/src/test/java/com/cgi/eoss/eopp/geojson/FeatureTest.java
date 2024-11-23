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

package com.cgi.eoss.eopp.geojson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.hash.HashCode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class FeatureTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final Feature FEATURE = Feature.builder().build();
    private static final String FEATURE_JSON = "{\"type\":\"Feature\",\"properties\":null,\"geometry\":null}";

    private static final Feature FEATURE_WITH_OPTIONAL_PROPERTIES = Feature.builder()
            .id("ee55e5b6-2d30-4da8-9a1d-897222403da1")
            .geometry(new Point(-0.1275, 51.507222))
            .property("stringProp", "badStringValue")
            .clearProperties()
            .property("stringProp", "stringValue")
            .properties(ImmutableMap.<String, Object>builder()
                    .put("intProp", Integer.MAX_VALUE)
                    .put("longProp", Long.MAX_VALUE)
                    .put("complexProp", ImmutableMap.<String, Object>builder()
                            .put("stringProp", "stringValue")
                            .put("intProp", Integer.MAX_VALUE)
                            .put("longProp", Long.MAX_VALUE)
                            .put("complexProp", ImmutableMap.<String, Object>builder()
                                    .put("stringProp", "stringValue")
                                    .build())
                            .build())
                    .build())
            .foreignMember("foo", "bar")
            .buildWithComputedBbox().toBuilder().build();
    private static final String FEATURE_WITH_OPTIONAL_PROPERTIES_JSON = "{\"type\":\"Feature\",\"id\":\"ee55e5b6-2d30-4da8-9a1d-897222403da1\",\"properties\":{\"stringProp\":\"stringValue\",\"intProp\":2147483647,\"longProp\":9223372036854775807,\"complexProp\":{\"stringProp\":\"stringValue\",\"intProp\":2147483647,\"longProp\":9223372036854775807,\"complexProp\":{\"stringProp\":\"stringValue\"}}},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-0.1275,51.507222]},\"bbox\":[-0.1275,51.507222,-0.1275,51.507222],\"foo\":\"bar\"}";

    @Test
    public void testSerializationAndDeserialization() throws IOException {
        String objAsJson = OBJECT_MAPPER.writeValueAsString(FEATURE);
        assertThat(objAsJson).isEqualTo(FEATURE_JSON);

        Feature objFromJson = OBJECT_MAPPER.readValue(FEATURE_JSON, Feature.class);
        assertThat(objFromJson).isEqualTo(FEATURE);
    }

    @Test
    public void testSerializationAndDeserializationWithOptionalProperties() throws IOException {
//        String objAsJson = OBJECT_MAPPER.writeValueAsString(FEATURE_WITH_OPTIONAL_PROPERTIES);
//        assertThat(objAsJson).isEqualTo(FEATURE_WITH_OPTIONAL_PROPERTIES_JSON);

        Feature objFromJson = OBJECT_MAPPER.readValue(FEATURE_WITH_OPTIONAL_PROPERTIES_JSON, Feature.class);
        assertThat(objFromJson).isEqualTo(FEATURE_WITH_OPTIONAL_PROPERTIES);
    }

    @Test
    public void testImmutability() throws IOException {
        Feature feature = OBJECT_MAPPER.readValue(FEATURE_WITH_OPTIONAL_PROPERTIES_JSON, Feature.class);
        try {
            feature.getProperties().put("key", "value");
            fail("Expected UnsupportedOperationException");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @Test
    public void testSpecConstraints() {
        try {
            new Feature(null, null, HashCode.fromInt(0), null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).startsWith("A Feature's \"id\" must be a JSON string or number");
        }
    }

    @Test
    public void testSchema() throws IOException, URISyntaxException {
        JsonSchema schema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7)
                .getSchema(getClass().getResource("/geojson-schema/Feature.json").toURI());

        JsonNode node = OBJECT_MAPPER.readTree(FEATURE_JSON);
        Set<ValidationMessage> errors = schema.validate(node);
        assertThat(errors).hasSize(0);

        node = OBJECT_MAPPER.readTree(FEATURE_WITH_OPTIONAL_PROPERTIES_JSON);
        errors = schema.validate(node);
        assertThat(errors).hasSize(0);
    }

}
