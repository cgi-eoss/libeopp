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

import java.util.Arrays;

import static com.google.common.truth.Truth.assertThat;

@RunWith(JUnit4.class)
public class GeoJSONTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    // Test data from the GeoJSON Spec: https://tools.ietf.org/html/rfc7946#appendix-A

    @Test
    public void testPoints() throws Exception {
        Point expected = Point.builder()
                .coordinates(new Position(100.0, 0.0))
                .build();
        assertThat(OBJECT_MAPPER.readValue(GeoJSON.class.getResourceAsStream("/point.geojson"), GeoJSON.class))
                .isEqualTo(expected);
    }

    @Test
    public void testLineStrings() throws Exception {
        LineString expected = LineString.builder()
                .coordinate(new Position(100.0, 0.0))
                .coordinate(new Position(101.0, 1.0))
                .build();
        assertThat(OBJECT_MAPPER.readValue(GeoJSON.class.getResourceAsStream("/linestring.geojson"), GeoJSON.class))
                .isEqualTo(expected);
    }

    @Test
    public void testPolygons() throws Exception {
        Polygon expected = Polygon.builder()
                .coordinate(LinearRing.builder()
                        .coordinate(new Position(100.0, 0.0))
                        .coordinate(new Position(101.0, 0.0))
                        .coordinate(new Position(101.0, 1.0))
                        .coordinate(new Position(100.0, 1.0))
                        .coordinate(new Position(100.0, 0.0))
                        .build())
                .build();
        assertThat(OBJECT_MAPPER.readValue(GeoJSON.class.getResourceAsStream("/polygon.geojson"), GeoJSON.class))
                .isEqualTo(expected);
    }

    @Test
    public void testPolygonWithHoles() throws Exception {
        Polygon expected = Polygon.builder()
                .coordinate(LinearRing.builder()
                        .coordinate(new Position(100.0, 0.0))
                        .coordinate(new Position(101.0, 0.0))
                        .coordinate(new Position(101.0, 1.0))
                        .coordinate(new Position(100.0, 1.0))
                        .coordinate(new Position(100.0, 0.0))
                        .build())
                .coordinate(LinearRing.builder()
                        .coordinate(new Position(100.8, 0.8))
                        .coordinate(new Position(100.8, 0.2))
                        .coordinate(new Position(100.2, 0.2))
                        .coordinate(new Position(100.2, 0.8))
                        .coordinate(new Position(100.8, 0.8))
                        .build())
                .build();
        assertThat(OBJECT_MAPPER.readValue(GeoJSON.class.getResourceAsStream("/polygon-with-holes.geojson"), GeoJSON.class))
                .isEqualTo(expected);
    }

    @Test
    public void testMultiPoints() throws Exception {
        MultiPoint expected = MultiPoint.builder()
                .coordinate(new Position(100.0, 0.0))
                .coordinate(new Position(101.0, 1.0))
                .build();
        assertThat(OBJECT_MAPPER.readValue(GeoJSON.class.getResourceAsStream("/multipoint.geojson"), GeoJSON.class))
                .isEqualTo(expected);
    }

    @Test
    public void testMultiLineStrings() throws Exception {
        MultiLineString expected = MultiLineString.builder()
                .coordinate(Arrays.asList(
                        new Position(100.0, 0.0),
                        new Position(101.0, 1.0)))
                .coordinate(Arrays.asList(
                        new Position(102.0, 2.0),
                        new Position(103.0, 3.0)))
                .build();
        assertThat(OBJECT_MAPPER.readValue(GeoJSON.class.getResourceAsStream("/multilinestring.geojson"), GeoJSON.class))
                .isEqualTo(expected);
    }

    @Test
    public void testMultiPolygons() throws Exception {
        MultiPolygon expected = MultiPolygon.builder()
                .polygon(Polygon.builder()
                        .coordinate(LinearRing.builder()
                                .coordinate(new Position(102.0, 2.0))
                                .coordinate(new Position(103.0, 2.0))
                                .coordinate(new Position(103.0, 3.0))
                                .coordinate(new Position(102.0, 3.0))
                                .coordinate(new Position(102.0, 2.0))
                                .build())
                        .build())
                .polygon(Polygon.builder()
                        .coordinate(LinearRing.builder()
                                .coordinate(new Position(100.0, 0.0))
                                .coordinate(new Position(101.0, 0.0))
                                .coordinate(new Position(101.0, 1.0))
                                .coordinate(new Position(100.0, 1.0))
                                .coordinate(new Position(100.0, 0.0))
                                .build())
                        .coordinate(LinearRing.builder()
                                .coordinate(new Position(100.2, 0.2))
                                .coordinate(new Position(100.2, 0.8))
                                .coordinate(new Position(100.8, 0.8))
                                .coordinate(new Position(100.8, 0.2))
                                .coordinate(new Position(100.2, 0.2))
                                .build())
                        .build())
                .build();
        assertThat(OBJECT_MAPPER.readValue(GeoJSON.class.getResourceAsStream("/multipolygon.geojson"), GeoJSON.class))
                .isEqualTo(expected);
    }

    @Test
    public void testGeometryCollections() throws Exception {
        GeometryCollection expected = GeometryCollection.builder()
                .geometry(Point.builder()
                        .coordinates(new Position(100.0, 0.0))
                        .build())
                .geometry(LineString.builder()
                        .coordinate(new Position(101.0, 0.0))
                        .coordinate(new Position(102.0, 1.0))
                        .build())
                .build();
        assertThat(OBJECT_MAPPER.readValue(GeoJSON.class.getResourceAsStream("/geometrycollection.geojson"), GeoJSON.class))
                .isEqualTo(expected);
    }

}
