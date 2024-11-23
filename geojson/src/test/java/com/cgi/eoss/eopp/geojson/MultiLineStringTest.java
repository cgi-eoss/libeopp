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
import com.google.common.collect.ImmutableList;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Set;

import static com.google.common.truth.Truth.assertThat;

@RunWith(JUnit4.class)
public class MultiLineStringTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final MultiLineString MULTI_LINE_STRING = MultiLineString.builder()
            .coordinate(LineString.builder()
                    .coordinate(new Position(-0.1275, 51.507222))
                    .coordinate(new Position(-0.1275, 52.507222))
                    .coordinate(new Position(0.1275, 52.507222))
                    .coordinate(new Position(-0.1275, 51.507222))
                    .build().getCoordinates())
            .lineString(LineString.builder()
                    .coordinate(new Position(-0.1275, 51.507222))
                    .coordinate(new Position(-0.1275, 52.507222))
                    .coordinate(new Position(0.1275, 52.507222))
                    .coordinate(new Position(-0.1275, 51.507222))
                    .build())
            .build();
    private static final String MULTI_LINE_STRING_JSON = "{\"type\":\"MultiLineString\",\"coordinates\":[[[-0.1275,51.507222],[-0.1275,52.507222],[0.1275,52.507222],[-0.1275,51.507222]],[[-0.1275,51.507222],[-0.1275,52.507222],[0.1275,52.507222],[-0.1275,51.507222]]]}";

    private static final MultiLineString MULTI_LINE_STRING_WITH_OPTIONAL_PROPERTIES = MultiLineString.builder()
            .coordinates(Collections.singleton(LineString.builder()
                    .coordinate(new Position(-0.1275, 51.507222, 1.0))
                    .coordinate(new Position(-0.1275, 52.507222, 23.99))
                    .coordinate(new Position(0.1275, 52.507222, 23.99))
                    .coordinate(new Position(-0.1275, 51.507222, 1.0))
                    .build().getCoordinates()))
            .lineStrings(Collections.singleton(LineString.builder()
                    .coordinate(new Position(-100.1275, 151.507222, 101.0))
                    .coordinate(new Position(-100.1275, 152.507222, 123.99))
                    .coordinate(new Position(100.1275, 152.507222, 123.99))
                    .coordinate(new Position(-100.1275, 151.507222, 101.0))
                    .build()))
            .foreignMember("foo", "bar")
            .buildWithComputedBbox().toBuilder().build();
    private static final String MULTI_LINE_STRING_WITH_OPTIONAL_PROPERTIES_JSON = "{\"type\":\"MultiLineString\",\"coordinates\":[" +
                                                                                  "[[-0.1275,51.507222,1.0],[-0.1275,52.507222,23.99],[0.1275,52.507222,23.99],[-0.1275,51.507222,1.0]]," +
                                                                                  "[[-100.1275,151.507222,101.0],[-100.1275,152.507222,123.99],[100.1275,152.507222,123.99],[-100.1275,151.507222,101.0]]" +
                                                                                  "]," +
                                                                                  "\"bbox\":[-100.1275,51.507222,1.0,100.1275,152.507222,123.99]," +
                                                                                  "\"foo\":\"bar\"}";

    @Test
    public void testSerializationAndDeserialization() throws IOException {
        String objAsJson = OBJECT_MAPPER.writeValueAsString(MULTI_LINE_STRING);
        assertThat(objAsJson).isEqualTo(MULTI_LINE_STRING_JSON);

        GeoJSON objFromJson = OBJECT_MAPPER.readValue(MULTI_LINE_STRING_JSON, GeoJSON.class);
        assertThat(objFromJson).isEqualTo(MULTI_LINE_STRING);
    }

    @Test
    public void testSerializationAndDeserializationWithOptionalProperties() throws IOException {
        String objAsJson = OBJECT_MAPPER.writeValueAsString(MULTI_LINE_STRING_WITH_OPTIONAL_PROPERTIES);
        assertThat(objAsJson).isEqualTo(MULTI_LINE_STRING_WITH_OPTIONAL_PROPERTIES_JSON);

        GeoJSON objFromJson = OBJECT_MAPPER.readValue(MULTI_LINE_STRING_WITH_OPTIONAL_PROPERTIES_JSON, GeoJSON.class);
        assertThat(objFromJson).isEqualTo(MULTI_LINE_STRING_WITH_OPTIONAL_PROPERTIES);
    }

    @Test
    public void testToLineStrings() {
        ImmutableList<LineString> lineStrings = ImmutableList.of(
                LineString.builder()
                        .coordinate(new Position(-0.1275, 51.507222))
                        .coordinate(new Position(-0.1275, 52.507222))
                        .coordinate(new Position(0.1275, 52.507222))
                        .coordinate(new Position(-0.1275, 51.507222))
                        .build(),
                LineString.builder()
                        .coordinate(new Position(-0.1275, 51.507222))
                        .coordinate(new Position(-0.1275, 52.507222))
                        .coordinate(new Position(0.1275, 52.507222))
                        .coordinate(new Position(-0.1275, 51.507222))
                        .build());
        assertThat(MULTI_LINE_STRING.toLineStrings()).isEqualTo(lineStrings);
    }

    @Test
    public void testSchema() throws IOException, URISyntaxException {
        JsonSchema schema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7)
                .getSchema(getClass().getResource("/geojson-schema/MultiLineString.json").toURI());

        JsonNode node = OBJECT_MAPPER.readTree(MULTI_LINE_STRING_JSON);
        Set<ValidationMessage> errors = schema.validate(node);
        assertThat(errors).hasSize(0);

        node = OBJECT_MAPPER.readTree(MULTI_LINE_STRING_WITH_OPTIONAL_PROPERTIES_JSON);
        errors = schema.validate(node);
        assertThat(errors).hasSize(0);
    }

}