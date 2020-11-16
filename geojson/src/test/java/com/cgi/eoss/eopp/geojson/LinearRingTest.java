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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class LinearRingTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final LinearRing LINEAR_RING = LinearRing.builder()
            .coordinate(new Position(-0.1275, 51.507222))
            .coordinate(new Position(-0.1275, 52.507222))
            .coordinate(new Position(0.1275, 52.507222))
            .coordinate(new Position(-0.1275, 51.507222))
            .build();
    private static final String LINEAR_RING_JSON = "[[-0.1275,51.507222],[-0.1275,52.507222],[0.1275,52.507222],[-0.1275,51.507222]]";

    @Test
    public void testSerializationAndDeserialization() throws IOException {
        String objAsJson = OBJECT_MAPPER.writeValueAsString(LINEAR_RING);
        assertThat(objAsJson).isEqualTo(LINEAR_RING_JSON);

        LinearRing objFromJson = OBJECT_MAPPER.readValue(LINEAR_RING_JSON, LinearRing.class);
        assertThat(objFromJson).isEqualTo(LINEAR_RING);
    }

    @Test
    public void testSpecConstraints() {
        try {
            LinearRing.builder()
                    .coordinate(new Position(-0.1275, 51.507222))
                    .coordinate(new Position(-0.1275, 52.507222))
                    .coordinate(new Position(0.1275, 52.507222))
                    .build();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("LinearRings require four or more positions, found 3");
        }

        try {
            LinearRing.builder()
                    .coordinate(new Position(-0.1275, 51.507222))
                    .coordinate(new Position(-0.1275, 52.507222))
                    .coordinate(new Position(0.1275, 52.507222))
                    .coordinate(new Position(-0.1275, 51.0000))
                    .build();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).startsWith("LinearRings require the first and last positions to be equivalent");
        }
    }

    @Test
    public void testToLineString() {
        LineString lineString = LINEAR_RING.toLineString();
        assertThat(lineString.getCoordinates()).isEqualTo(LINEAR_RING.getCoordinates());
        assertThat(lineString.getBbox()).isNull();
        assertThat(lineString.getForeignMembers()).isEmpty();
    }

}