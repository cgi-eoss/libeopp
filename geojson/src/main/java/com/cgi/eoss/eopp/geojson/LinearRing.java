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
import com.fasterxml.jackson.annotation.JsonValue;
import org.locationtech.jts.geom.Coordinate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>A linear ring is a type of {@link LineString}, but is not itself a {@link GeoJSON} geometry type. It is specified
 * for use in Polygons.</p>
 *
 * @see <a href="https://tools.ietf.org/html/rfc7946#section-3.1.6">https://tools.ietf.org/html/rfc7946#section-3.1.6</a>
 */
public class LinearRing implements JtsGeometry<org.locationtech.jts.geom.LinearRing> {

    private final List<Position> coordinates;

    @JsonCreator
    public LinearRing(List<Position> coordinates) {
        this.coordinates = Optional.ofNullable(coordinates).map(Collections::unmodifiableList)
                .orElseThrow(() -> new IllegalArgumentException("LinearRing is missing required 'coordinates' property"));

        // RFC7946: A linear ring is a closed LineString with four or more positions.
        if (this.coordinates.size() < 4) {
            throw new IllegalArgumentException(String.format("LinearRings require four or more positions, found %s", coordinates.size()));
        }
        // RFC7496: The first and last positions are equivalent, and they MUST
        // contain identical values; their representation SHOULD also be identical.
        // (Position#equals measures value equivalence)
        if (!this.coordinates.get(0).equals(this.coordinates.get(this.coordinates.size() - 1))) {
            throw new IllegalArgumentException(String.format("LinearRings require the first and last positions to be equivalent, found %s", coordinates));
        }
    }

    public static LinearRing fromLineString(LineString lineString) {
        return new BuilderImpl().coordinates(lineString.getCoordinates()).build();
    }

    public static Builder builder() {
        return new BuilderImpl();
    }

    @JsonValue
    public List<Position> getCoordinates() {
        return coordinates;
    }

    @Override
    public org.locationtech.jts.geom.LinearRing toJts() {
        return Geometry.DEFAULT_GEOMETRY_FACTORY.createLinearRing(
                coordinates.stream().map(Position::toJts).toArray(Coordinate[]::new)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinates);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinearRing that = (LinearRing) o;
        return Objects.equals(coordinates, that.coordinates);
    }

    @Override
    public String toString() {
        return "LinearRing{" +
               "coordinates=" + coordinates +
               '}';
    }

    public LineString toLineString() {
        return new LineString(null, null, coordinates);
    }

    public Builder toBuilder() {
        return new BuilderImpl()
                .coordinates(this.coordinates);
    }

    public interface Builder {
        Builder coordinate(Position coordinate);

        Builder coordinates(Collection<? extends Position> coordinates);

        Builder clearCoordinates();

        LinearRing build();
    }

    private static class BuilderImpl implements Builder {
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

        @Override
        public LinearRing build() {
            return new LinearRing(this.coordinates);
        }
    }
}
