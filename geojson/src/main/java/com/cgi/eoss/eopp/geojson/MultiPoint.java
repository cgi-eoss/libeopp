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

/**
 * <p>A Geometry construct comprising multiple {@link Position} objects.</p>
 *
 * @see <a href="https://tools.ietf.org/html/rfc7946#section-3.1.3">https://tools.ietf.org/html/rfc7946#section-3.1.3</a>
 */
@JsonPropertyOrder({"type", "coordinates", "bbox"})
public class MultiPoint extends Geometry<List<Position>, org.locationtech.jts.geom.MultiPoint> {

    private final List<Position> coordinates;

    @JsonCreator
    public MultiPoint(
            @JsonProperty("bbox") List<BigDecimal> bbox,
            @JsonAnySetter @JsonProperty("foreignMembers") Map<String, Object> foreignMembers,
            @JsonProperty("coordinates") List<Position> coordinates) {
        super(GeoJSONType.MultiPoint, bbox, foreignMembers);

        this.coordinates = Optional.ofNullable(coordinates).map(Collections::unmodifiableList)
                .orElseThrow(() -> new IllegalArgumentException("MultiPoint is missing required 'coordinates' property"));
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
        return flattenCoordinates(coordinates).toList();
    }

    @Override
    public org.locationtech.jts.geom.MultiPoint toJts() {
        return Geometry.DEFAULT_GEOMETRY_FACTORY.createMultiPointFromCoords(
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
        MultiPoint that = (MultiPoint) o;
        return Objects.equals(coordinates, that.coordinates);
    }

    @Override
    public String toString() {
        return "MultiPoint{" +
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

    public interface Builder extends GeoJSON.Builder<MultiPoint, MultiPoint.Builder> {
        Builder coordinate(Position coordinate);

        Builder coordinates(Collection<? extends Position> coordinates);

        Builder clearCoordinates();

        MultiPoint build();
    }

    public static class BuilderImpl extends GeoJSON.BuilderImpl<MultiPoint, MultiPoint.Builder> implements Builder {
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
        public MultiPoint build() {
            return new MultiPoint(getBbox(), getForeignMembers(), this.coordinates);
        }

        @Override
        public MultiPoint buildWithComputedBbox() {
            if (coordinates != null && !coordinates.isEmpty()) {
                bbox(GeoJSONUtil.computeBbox(flattenCoordinates(coordinates)));
            }
            return build();
        }
    }
}
