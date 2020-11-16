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
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.Set;

import static com.google.common.truth.Truth.assertThat;

@RunWith(JUnit4.class)
public class PointTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final Point POINT = new Point(-0.1275, 51.507222);
    private static final String POINT_JSON = "{\"type\":\"Point\",\"coordinates\":[-0.1275,51.507222]}";

    private static final Point POINT_WITH_OPTIONAL_PROPERTIES = Point.builder()
            .coordinates(new Position(-0.1275, 51.507222, 1.0))
            .foreignMember("foo", "bar")
            .buildWithComputedBbox().toBuilder().build();
    private static final String POINT_WITH_OPTIONAL_PROPERTIES_JSON = "{\"type\":\"Point\",\"coordinates\":[-0.1275,51.507222,1.0],\"bbox\":[-0.1275,51.507222,1.0,-0.1275,51.507222,1.0],\"foo\":\"bar\"}";

    private static final Point PRECISE_POINT = new Point(new BigDecimal("-2.029398092"), new BigDecimal("55.768841938"));
    private static final String PRECISE_POINT_JSON_TRUNC = "{\"type\":\"Point\",\"coordinates\":[-2.029398092,55.768841938]}";
    private static final String PRECISE_POINT_JSON = "{\"type\":\"Point\",\"coordinates\":[-2.029398092000008,55.76884193799998]}";

    private static final Point ANOTHER_PRECISE_POINT = new Point(new BigDecimal("-0.027053057"), new BigDecimal("53.780703107"));
    private static final String ANOTHER_PRECISE_POINT_JSON_TRUNC = "{\"type\":\"Point\",\"coordinates\":[-0.027053057,53.780703107]}";
    private static final String ANOTHER_PRECISE_POINT_JSON = "{\"type\":\"Point\",\"coordinates\":[-0.027053056999989167,53.78070310700002]}";

    @Test
    public void testSerializationAndDeserialization() throws IOException {
        String objAsJson = OBJECT_MAPPER.writeValueAsString(POINT);
        assertThat(objAsJson).isEqualTo(POINT_JSON);

        GeoJSON objFromJson = OBJECT_MAPPER.readValue(POINT_JSON, GeoJSON.class);
        assertThat(objFromJson).isEqualTo(POINT);
    }

    @Test
    public void testSerializationAndDeserializationWithOptionalProperties() throws IOException {
        String objAsJson = OBJECT_MAPPER.writeValueAsString(POINT_WITH_OPTIONAL_PROPERTIES);
        assertThat(objAsJson).isEqualTo(POINT_WITH_OPTIONAL_PROPERTIES_JSON);

        GeoJSON objFromJson = OBJECT_MAPPER.readValue(POINT_WITH_OPTIONAL_PROPERTIES_JSON, GeoJSON.class);
        assertThat(objFromJson).isEqualTo(POINT_WITH_OPTIONAL_PROPERTIES);
    }

    @Test
    public void testPrecision() throws IOException {
        String objAsJson = OBJECT_MAPPER.writeValueAsString(PRECISE_POINT);
        assertThat(objAsJson).isEqualTo(PRECISE_POINT_JSON_TRUNC);

        GeoJSON objFromJson = OBJECT_MAPPER.readValue(PRECISE_POINT_JSON, GeoJSON.class);
        assertThat(objFromJson).isEqualTo(PRECISE_POINT);
    }

    @Test
    public void testMorePrecision() throws IOException {
        String objAsJson = OBJECT_MAPPER.writeValueAsString(ANOTHER_PRECISE_POINT);
        assertThat(objAsJson).isEqualTo(ANOTHER_PRECISE_POINT_JSON_TRUNC);

        GeoJSON objFromJson = OBJECT_MAPPER.readValue(ANOTHER_PRECISE_POINT_JSON, GeoJSON.class);
        assertThat(objFromJson).isEqualTo(ANOTHER_PRECISE_POINT);
    }

    @Test
    public void testSchema() throws IOException, URISyntaxException {
        JsonSchema schema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7)
                .getSchema(getClass().getResource("/geojson-schema/Point.json").toURI());

        JsonNode node = OBJECT_MAPPER.readTree(POINT_JSON);
        Set<ValidationMessage> errors = schema.validate(node);
        assertThat(errors).hasSize(0);

        node = OBJECT_MAPPER.readTree(POINT_WITH_OPTIONAL_PROPERTIES_JSON);
        errors = schema.validate(node);
        assertThat(errors).hasSize(0);
    }

}