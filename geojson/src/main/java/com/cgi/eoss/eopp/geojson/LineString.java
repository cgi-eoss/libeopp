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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.locationtech.jts.geom.Coordinate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * <p>A Geometry construct comprising two or more {@link Position} objects.</p>
 *
 * @see <a href="https://tools.ietf.org/html/rfc7946#section-3.1.4">https://tools.ietf.org/html/rfc7946#section-3.1.4</a>
 */
@JsonPropertyOrder({"type", "coordinates", "bbox"})
public class LineString extends Geometry<List<Position>, org.locationtech.jts.geom.LineString> {

    private final List<Position> coordinates;

    @JsonCreator
    public LineString(
            @JsonProperty("bbox") List<BigDecimal> bbox,
            @JsonProperty("foreignMembers") Map<String, Object> foreignMembers,
            @JsonProperty("coordinates") List<Position> coordinates) {
        super(GeoJSONType.LineString, bbox, foreignMembers);

        this.coordinates = Optional.ofNullable(coordinates).map(Collections::unmodifiableList)
                .orElseThrow(() -> new IllegalArgumentException("LineString is missing required 'coordinates' property"));

        // RFC7496: For type "LineString", the "coordinates" member is an array of two or more positions.
        if (this.coordinates.size() < 2) {
            throw new IllegalArgumentException(String.format("LineStrings require two or more positions, found %s", this.coordinates.size()));
        }
    }

    public static Builder builder() {
        return new BuilderImpl();
    }

    private static Stream<Position> flattenCoordinates(List<Position> coordinates) {
        return coordinates.stream();
    }

    @Override
    public List<Position> getCoordinates() {
        return coordinates;
    }

    @Override
    public List<Position> computeFlattenedCoordinates() {
        return flattenCoordinates(coordinates).collect(toList());
    }

    @Override
    public org.locationtech.jts.geom.LineString toJts() {
        return Geometry.DEFAULT_GEOMETRY_FACTORY.createLineString(
                coordinates.stream().map(Position::toJts).toArray(Coordinate[]::new)
        );
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
        LineString that = (LineString) o;
        return Objects.equals(coordinates, that.coordinates);
    }

    @Override
    public String toString() {
        return "LineString{" +
                "coordinates=" + coordinates +
                toStringProperties() +
                '}';
    }

    public LinearRing toLinearRing() {
        return new LinearRing(coordinates);
    }

    public Builder toBuilder() {
        return new BuilderImpl()
                .bbox(this.getBbox())
                .foreignMembers(this.getForeignMembers())
                .coordinates(this.coordinates);
    }

    public interface Builder extends GeoJSON.Builder<LineString, Builder> {
        LineString.Builder coordinate(Position coordinate);

        LineString.Builder coordinates(Collection<? extends Position> coordinates);

        LineString.Builder clearCoordinates();
    }

    static class BuilderImpl extends GeoJSON.BuilderImpl<LineString, Builder> implements Builder {
        private List<Position> coordinates;

        @Override
        public Builder coordinate(Position coordinate) {
            if (this.coordinates == null) this.coordinates = new ArrayList<>();
            this.coordinates.add(coordinate);
            return this;
        }

        @Override
        public Builder coordinates(Collection<? extends Position> coordinates) {
            if (this.coordinates == null) this.coordinates = new ArrayList<>();
            this.coordinates.addAll(coordinates);
            return this;
        }

        @Override
        public Builder clearCoordinates() {
            this.coordinates = null;
            return this;
        }

        public LineString build() {
            return new LineString(getBbox(), getForeignMembers(), this.coordinates);
        }

        @Override
        public LineString buildWithComputedBbox() {
            if (coordinates != null && !coordinates.isEmpty()) {
                bbox(GeoJSONUtil.computeBbox(flattenCoordinates(coordinates)));
            }
            return build();
        }
    }
}
