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
 * <p>A Geometry construct comprising multiple {@link Polygon} objects.</p>
 *
 * @see <a href="https://tools.ietf.org/html/rfc7946#section-3.1.7">https://tools.ietf.org/html/rfc7946#section-3.1.7</a>
 */
@JsonPropertyOrder({"type", "coordinates", "bbox"})
public class MultiPolygon extends Geometry<List<List<LinearRing>>, org.locationtech.jts.geom.MultiPolygon> {

    private final List<List<LinearRing>> coordinates;

    @JsonCreator
    public MultiPolygon(
            @JsonProperty("bbox") List<BigDecimal> bbox,
            @JsonProperty("foreignMembers") Map<String, Object> foreignMembers,
            @JsonProperty("coordinates") List<List<LinearRing>> coordinates) {
        super(GeoJSONType.MultiPolygon, bbox, foreignMembers);

        this.coordinates = Optional.ofNullable(coordinates).map(it -> Collections.unmodifiableList(it.stream().map(Collections::unmodifiableList).collect(toList())))
                .orElseThrow(() -> new IllegalArgumentException("MultiPolygon is missing required 'coordinates' property"));
    }

    public static Builder builder() {
        return new BuilderImpl();
    }

    private static Stream<Position> flattenCoordinates(List<List<LinearRing>> coordinates) {
        return coordinates.stream().flatMap(Collection::stream).flatMap(linearRing -> linearRing.getCoordinates().stream());
    }

    @Override
    public List<List<LinearRing>> getCoordinates() {
        return coordinates;
    }

    @Override
    public List<Position> computeFlattenedCoordinates() {
        return flattenCoordinates(coordinates).collect(toList());
    }

    @Override
    public org.locationtech.jts.geom.MultiPolygon toJts() {
        return DEFAULT_GEOMETRY_FACTORY.createMultiPolygon(
                coordinates.stream().map(polygon -> new Polygon(null, null, polygon).toJts()).toArray(org.locationtech.jts.geom.Polygon[]::new)
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
        MultiPolygon that = (MultiPolygon) o;
        return Objects.equals(coordinates, that.coordinates);
    }

    @Override
    public String toString() {
        return "MultiPolygon{" +
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

    public interface Builder extends GeoJSON.Builder<MultiPolygon, Builder> {
        Builder polygon(Polygon polygon);

        Builder coordinate(List<LinearRing> polygon);

        Builder polygons(Iterable<? extends Polygon> polygons);

        Builder coordinates(Iterable<List<LinearRing>> polygons);

        Builder clearCoordinates();
    }

    private static class BuilderImpl extends GeoJSON.BuilderImpl<MultiPolygon, Builder> implements Builder {
        private List<List<LinearRing>> coordinates;

        @Override
        public Builder polygon(Polygon polygon) {
            if (coordinates == null) coordinates = new ArrayList<>();
            this.coordinates.add(polygon.getCoordinates());
            return this;
        }

        @Override
        public Builder coordinate(List<LinearRing> polygon) {
            if (coordinates == null) coordinates = new ArrayList<>();
            this.coordinates.add(polygon);
            return this;
        }

        @Override
        public Builder polygons(Iterable<? extends Polygon> polygons) {
            if (coordinates == null) coordinates = new ArrayList<>();
            polygons.forEach(this::polygon);
            return this;
        }

        @Override
        public Builder coordinates(Iterable<List<LinearRing>> polygons) {
            if (coordinates == null) coordinates = new ArrayList<>();
            polygons.forEach(this::coordinate);
            return this;
        }

        @Override
        public Builder clearCoordinates() {
            this.coordinates = null;
            return this;
        }

        public MultiPolygon build() {
            return new MultiPolygon(getBbox(), getForeignMembers(), this.coordinates);
        }

        @Override
        public MultiPolygon buildWithComputedBbox() {
            if (coordinates != null && !coordinates.isEmpty()) {
                bbox(GeoJSONUtil.computeBbox(flattenCoordinates(coordinates)));
            }
            return build();
        }
    }
}
