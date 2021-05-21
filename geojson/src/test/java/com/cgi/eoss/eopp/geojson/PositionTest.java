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

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.locationtech.jts.geom.Coordinate;

import java.math.BigDecimal;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class PositionTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    public void testJsonMapping() throws Exception {
        assertBidirectionalJsonMapping(new Position(), "[0,0]");
        assertBidirectionalJsonMapping(new Position(1.0, 1.0), "[1.0,1.0]");
        assertBidirectionalJsonMapping(new Position(999999.999999, 999999.999999), "[999999.999999,999999.999999]");
        assertBidirectionalJsonMapping(new Position(BigDecimal.TEN, BigDecimal.TEN), "[10,10]");
        assertBidirectionalJsonMapping(new Position(BigDecimal.valueOf(1234567890, 5), BigDecimal.valueOf(1234567890, 5)), "[12345.67890,12345.67890]");
    }

    @Test
    public void testOptionalAltitude() throws Exception {
        assertBidirectionalJsonMapping(new Position(0.0, 0.0, 0.0), "[0.0,0.0,0.0]");
        assertBidirectionalJsonMapping(new Position(0, 0, Double.NaN), "[0.0,0.0]");
        assertBidirectionalJsonMapping(new Position(BigDecimal.ONE, BigDecimal.ONE, null), "[1,1]");
        assertBidirectionalJsonMapping(new Position(BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE), "[1,1,1]");
    }

    @Test
    public void testDeserializer() throws Exception {
        assertThat(OBJECT_MAPPER.readValue("[0,0,null]", Position.class)).isEqualTo(new Position());
    }

    @Test
    public void testDeserializerFailures() throws Exception {
        try {
            OBJECT_MAPPER.readValue("[0,null]", Position.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertThat(e.getMessage()).startsWith("Cannot construct instance of `com.cgi.eoss.eopp.geojson.Position`, problem: `java.lang.NullPointerException`");
        }

        try {
            OBJECT_MAPPER.readValue("[0,{}]", Position.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertThat(e.getMessage()).startsWith("Cannot deserialize value of type `java.math.BigDecimal` from Object value (token `JsonToken.START_OBJECT`)");
        }

        try {
            OBJECT_MAPPER.readValue("{\"longitude\":0,\"latitude\":0}", Position.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertThat(e.getMessage()).startsWith("Cannot deserialize value of type `[Ljava.math.BigDecimal;` from Object value (token `JsonToken.FIELD_NAME`)");
        }
    }

    @Test
    public void testGetDimensions() throws Exception {
        assertThat(new Position(0.0, 0.0).getDimensions()).isEqualTo(2);
        assertThat(new Position(0.0, 0.0, 0.0).getDimensions()).isEqualTo(3);
    }

    @Test
    public void testMaxPrecision() throws Exception {
        assertThat(new Position(0.0, 0.0).getMaxPrecision()).isEqualTo(1);
        assertThat(new Position(BigDecimal.ZERO, BigDecimal.valueOf(1234567890, 5), BigDecimal.TEN).getMaxPrecision()).isEqualTo(5);
        assertThat(new Position(BigDecimal.ZERO, BigDecimal.valueOf(12345, 3), BigDecimal.valueOf(1234567890, 6)).getMaxPrecision()).isEqualTo(6);
    }

    @Test
    public void testToOpengis() throws Exception {
        assertThat(new Position().toJts()).isEqualTo(new Coordinate());
        assertThat(new Position(1.0, 1.0).toJts()).isEqualTo(new Coordinate(1.0, 1.0));
        assertThat(new Position(0.0, 0.0, 0.0).toJts()).isEqualTo(new Coordinate(0.0, 0.0, 0.0));
    }

    @Test
    public void testGetElements() throws Exception {
        assertThat(new Position().getElements()).asList().containsExactly(BigDecimal.valueOf(0), BigDecimal.valueOf(0)).inOrder();
        assertThat(new Position(1.00, 0.0, -1.0).getElements()).asList().containsExactly(BigDecimal.valueOf(1.00), BigDecimal.valueOf(0.0), BigDecimal.valueOf(-1.0)).inOrder();
    }

    @Test
    public void testBuilder() {
        assertThat(Position.builder().longitude(BigDecimal.valueOf(1.00)).latitude(BigDecimal.valueOf(0.0)).build()).isEqualTo(new Position(1.00, 0.0, Double.NaN));
        assertThat(Position.builder().longitude(BigDecimal.valueOf(1.00)).latitude(BigDecimal.valueOf(0.0)).altitude(BigDecimal.TEN).build()).isEqualTo(new Position(1.00, 0.0, 10.0));

        Position position = new Position(1.00, 0.0, 10.0);
        assertThat(position.toBuilder().build()).isEqualTo(position);

        try {
            Position.builder().build();
            fail("Expected IllegalArgumentException");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
            assertThat(e).hasMessageThat().isEqualTo("A Position requires both longitude and latitude, received null and null");
        }
    }

    private void assertBidirectionalJsonMapping(Position position, String expectedString) throws Exception {
        String stringValue = OBJECT_MAPPER.writeValueAsString(position);
        assertThat(stringValue).isEqualTo(expectedString);
        assertThat(OBJECT_MAPPER.readValue(stringValue, Position.class)).isEqualTo(position);
    }

}