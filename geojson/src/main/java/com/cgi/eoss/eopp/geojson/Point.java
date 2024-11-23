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

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * <p>A Geometry construct with plain coordinates, i.e. a {@link Position}.</p>
 *
 * @see <a href="https://tools.ietf.org/html/rfc7946#section-3.1.2">https://tools.ietf.org/html/rfc7946#section-3.1.2</a>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "coordinates", "bbox"})
public class Point extends Geometry<Position, org.locationtech.jts.geom.Point> {

    private final Position coordinates;

    public Point(Position coordinates) {
        this(null, null, coordinates);
    }

    public Point(double longitude, double latitude) {
        this(null, null, new Position(longitude, latitude));
    }

    public Point(BigDecimal longitude, BigDecimal latitude) {
        this(null, null, new Position(longitude, latitude));
    }

    @JsonCreator
    public Point(
            @JsonProperty("bbox") List<BigDecimal> bbox,
            @JsonAnySetter @JsonProperty("foreignMembers") Map<String, Object> foreignMembers,
            @JsonProperty("coordinates") Position coordinates) {
        super(GeoJSONType.Point, bbox, foreignMembers);

        this.coordinates = Optional.ofNullable(coordinates)
                .orElseThrow(() -> new IllegalArgumentException("Point is missing required 'coordinates' property"));
    }

    public static Builder builder() {
        return new BuilderImpl();
    }

    private static Stream<Position> flattenCoordinates(Position coordinates) {
        return Stream.of(coordinates);
    }

    public Position getCoordinates() {
        return coordinates;
    }

    @Override
    public List<Position> computeFlattenedCoordinates() {
        return flattenCoordinates(coordinates).toList();
    }

    @Override
    public org.locationtech.jts.geom.Point toJts() {
        return Geometry.DEFAULT_GEOMETRY_FACTORY.createPoint(coordinates.toJts());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), coordinates);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Point point = (Point) o;
        return Objects.equals(coordinates, point.coordinates);
    }

    @Override
    public String toString() {
        return "Point{" +
               "coordinates=" + coordinates +
               toStringProperties() +
               '}';
    }

    public Builder toBuilder() {
        return new BuilderImpl()
                .bbox(this.getBbox())
                .foreignMembers(this.getForeignMembers())
                .coordinates(this.coordinates);
    }

    public interface Builder extends GeoJSON.Builder<Point, Point.Builder> {
        Point.Builder coordinates(Position position);
    }

    private static class BuilderImpl extends GeoJSON.BuilderImpl<Point, Point.Builder> implements Builder {
        private Position coordinates;

        public Builder coordinates(Position coordinates) {
            this.coordinates = coordinates;
            return this;
        }

        public Point build() {
            return new Point(getBbox(), getForeignMembers(), coordinates);
        }

        @Override
        public Point buildWithComputedBbox() {
            if (coordinates != null) {
                // the SW/NE corners are identical; just repeat the elements of this position
                bbox(GeoJSONUtil.computeBbox(flattenCoordinates(coordinates)));
            }
            return build();
        }
    }
}
