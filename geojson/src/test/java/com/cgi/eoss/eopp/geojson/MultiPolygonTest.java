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
public class MultiPolygonTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final MultiPolygon MULTI_POLYGON = MultiPolygon.builder()
            .polygon(Polygon.builder().coordinate(LinearRing.builder()
                    .coordinate(new Position(-0.1275, 51.507222))
                    .coordinate(new Position(-0.1275, 52.507222))
                    .coordinate(new Position(0.1275, 52.507222))
                    .coordinate(new Position(-0.1275, 51.507222))
                    .build()).build())
            .coordinate(Polygon.builder().coordinate(LinearRing.builder()
                    .coordinate(new Position(-0.1275, 51.507222))
                    .coordinate(new Position(-0.1275, 52.507222))
                    .coordinate(new Position(0.1275, 52.507222))
                    .coordinate(new Position(-0.1275, 51.507222))
                    .build()).build().getCoordinates())
            .build();
    private static final String MULTI_POLYGON_JSON = "{\"type\":\"MultiPolygon\",\"coordinates\":[[[[-0.1275,51.507222],[-0.1275,52.507222],[0.1275,52.507222],[-0.1275,51.507222]]],[[[-0.1275,51.507222],[-0.1275,52.507222],[0.1275,52.507222],[-0.1275,51.507222]]]]}";

    private static final MultiPolygon MULTI_POLYGON_WITH_OPTIONAL_PROPERTIES = MultiPolygon.builder()
            .polygon(Polygon.builder().coordinate(LinearRing.builder()
                    .coordinate(new Position(-0.1275, 51.507222, 1.0))
                    .coordinate(new Position(-0.1275, 52.507222, 23.99))
                    .coordinate(new Position(0.1275, 52.507222, 23.99))
                    .coordinate(new Position(-0.1275, 51.507222, 1.0))
                    .build()).build())
            .coordinate(Polygon.builder().coordinate(LinearRing.builder()
                    .coordinate(new Position(-100.1275, 151.507222, 101.0))
                    .coordinate(new Position(-100.1275, 152.507222, 123.99))
                    .coordinate(new Position(100.1275, 152.507222, 123.99))
                    .coordinate(new Position(-100.1275, 151.507222, 101.0))
                    .build()).build().getCoordinates())
            .foreignMember("foo", "bar")
            .buildWithComputedBbox().toBuilder().build();
    private static final String MULTI_POLYGON_WITH_OPTIONAL_PROPERTIES_JSON = "{\"type\":\"MultiPolygon\",\"coordinates\":[" +
                                                                              "[[[-0.1275,51.507222,1.0],[-0.1275,52.507222,23.99],[0.1275,52.507222,23.99],[-0.1275,51.507222,1.0]]]," +
                                                                              "[[[-100.1275,151.507222,101.0],[-100.1275,152.507222,123.99],[100.1275,152.507222,123.99],[-100.1275,151.507222,101.0]]]" +
                                                                              "],\"bbox\":[-100.1275,51.507222,1.0,100.1275,152.507222,123.99],\"foo\":\"bar\"}";

    @Test
    public void testSerializationAndDeserialization() throws IOException {
        String objAsJson = OBJECT_MAPPER.writeValueAsString(MULTI_POLYGON);
        assertThat(objAsJson).isEqualTo(MULTI_POLYGON_JSON);

        GeoJSON objFromJson = OBJECT_MAPPER.readValue(MULTI_POLYGON_JSON, GeoJSON.class);
        assertThat(objFromJson).isEqualTo(MULTI_POLYGON);
    }

    @Test
    public void testSerializationAndDeserializationWithOptionalProperties() throws IOException {
        String objAsJson = OBJECT_MAPPER.writeValueAsString(MULTI_POLYGON_WITH_OPTIONAL_PROPERTIES);
        assertThat(objAsJson).isEqualTo(MULTI_POLYGON_WITH_OPTIONAL_PROPERTIES_JSON);

        GeoJSON objFromJson = OBJECT_MAPPER.readValue(MULTI_POLYGON_WITH_OPTIONAL_PROPERTIES_JSON, GeoJSON.class);
        assertThat(objFromJson).isEqualTo(MULTI_POLYGON_WITH_OPTIONAL_PROPERTIES);
    }

    @Test
    public void testSchema() throws IOException, URISyntaxException {
        JsonSchema schema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7)
                .getSchema(getClass().getResource("/geojson-schema/MultiPolygon.json").toURI());

        JsonNode node = OBJECT_MAPPER.readTree(MULTI_POLYGON_JSON);
        Set<ValidationMessage> errors = schema.validate(node);
        assertThat(errors).hasSize(0);

        node = OBJECT_MAPPER.readTree(MULTI_POLYGON_WITH_OPTIONAL_PROPERTIES_JSON);
        errors = schema.validate(node);
        assertThat(errors).hasSize(0);
    }

}