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

@RunWith(JUnit4.class)
public class MultiPointTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final MultiPoint MULTI_POINT = MultiPoint.builder()
            .coordinate(new Position(-0.1275, 51.507222))
            .coordinate(new Position(51.507222, -0.1275))
            .build();
    private static final String MULTI_POINT_JSON = "{\"type\":\"MultiPoint\",\"coordinates\":[[-0.1275,51.507222],[51.507222,-0.1275]]}";

    private static final MultiPoint MULTI_POINT_WITH_OPTIONAL_PROPERTIES = MultiPoint.builder()
            .coordinate(new Position(-0.1275, 51.507222, 1.0))
            .coordinate(new Position(51.507222, -0.1275, 23.99))
            .foreignMember("foo", "bar")
            .buildWithComputedBbox().toBuilder().build();
    private static final String MULTI_POINT_WITH_OPTIONAL_PROPERTIES_JSON = "{\"type\":\"MultiPoint\",\"coordinates\":[[-0.1275,51.507222,1.0],[51.507222,-0.1275,23.99]],\"bbox\":[-0.1275,-0.1275,1.0,51.507222,51.507222,23.99],\"foo\":\"bar\"}";

    @Test
    public void testSerializationAndDeserialization() throws IOException {
        String objAsJson = OBJECT_MAPPER.writeValueAsString(MULTI_POINT);
        assertThat(objAsJson).isEqualTo(MULTI_POINT_JSON);

        GeoJSON objFromJson = OBJECT_MAPPER.readValue(MULTI_POINT_JSON, GeoJSON.class);
        assertThat(objFromJson).isEqualTo(MULTI_POINT);
    }

    @Test
    public void testSerializationAndDeserializationWithOptionalProperties() throws IOException {
        String objAsJson = OBJECT_MAPPER.writeValueAsString(MULTI_POINT_WITH_OPTIONAL_PROPERTIES);
        assertThat(objAsJson).isEqualTo(MULTI_POINT_WITH_OPTIONAL_PROPERTIES_JSON);

        GeoJSON objFromJson = OBJECT_MAPPER.readValue(MULTI_POINT_WITH_OPTIONAL_PROPERTIES_JSON, GeoJSON.class);
        assertThat(objFromJson).isEqualTo(MULTI_POINT_WITH_OPTIONAL_PROPERTIES);
    }

    @Test
    public void testSchema() throws IOException, URISyntaxException {
        JsonSchema schema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7)
                .getSchema(getClass().getResource("/geojson-schema/MultiPoint.json").toURI());

        JsonNode node = OBJECT_MAPPER.readTree(MULTI_POINT_JSON);
        Set<ValidationMessage> errors = schema.validate(node);
        assertThat(errors).hasSize(0);

        node = OBJECT_MAPPER.readTree(MULTI_POINT_WITH_OPTIONAL_PROPERTIES_JSON);
        errors = schema.validate(node);
        assertThat(errors).hasSize(0);
    }

}