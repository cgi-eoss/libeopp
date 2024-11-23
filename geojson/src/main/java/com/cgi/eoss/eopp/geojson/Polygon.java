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
 * <p>A Geometry construct comprising one or more {@link LinearRing} objects.</p>
 *
 * @see <a href="https://tools.ietf.org/html/rfc7946#section-3.1.6">https://tools.ietf.org/html/rfc7946#section-3.1.6</a>
 */
@JsonPropertyOrder({"type", "coordinates"})
public class Polygon extends Geometry<List<LinearRing>, org.locationtech.jts.geom.Polygon> {

    private final List<LinearRing> coordinates;

    @JsonCreator
    public Polygon(
            @JsonProperty("bbox") List<BigDecimal> bbox,
            @JsonAnySetter @JsonProperty("foreignMembers") Map<String, Object> foreignMembers,
            @JsonProperty("coordinates") List<LinearRing> coordinates) {
        super(GeoJSONType.Polygon, bbox, foreignMembers);

        this.coordinates = Optional.ofNullable(coordinates).map(Collections::unmodifiableList)
                .orElseThrow(() -> new IllegalArgumentException("Polygon is missing required 'coordinates' property"));

        // RFC7946: For backwards compatibility, parsers SHOULD NOT reject
        // Polygons that do not follow the right-hand rule.
        // (We do not check this)

        // RFC7946: For Polygons with more than one of these rings, the first
        // MUST be the exterior ring, and any others MUST be interior rings.
        // The exterior ring bounds the surface, and the interior rings (if
        // present) bound holes within the surface.
        //TODO Can this be checked?
    }

    public static Builder builder() {
        return new BuilderImpl();
    }

    private static Stream<Position> flattenCoordinates(List<LinearRing> coordinates) {
        return coordinates.stream().flatMap(linearRing -> linearRing.getCoordinates().stream());
    }

    @Override
    public List<LinearRing> getCoordinates() {
        return coordinates;
    }

    @Override
    public List<Position> computeFlattenedCoordinates() {
        return flattenCoordinates(coordinates).toList();
    }

    @Override
    public org.locationtech.jts.geom.Polygon toJts() {
        return DEFAULT_GEOMETRY_FACTORY.createPolygon(
                coordinates.get(0).toJts(),
                coordinates.subList(1, coordinates.size()).stream().map(LinearRing::toJts).toArray(org.locationtech.jts.geom.LinearRing[]::new)
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
        Polygon polygon = (Polygon) o;
        return Objects.equals(coordinates, polygon.coordinates);
    }

    @Override
    public String toString() {
        return "Polygon{" +
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

    public interface Builder extends GeoJSON.Builder<Polygon, Builder> {
        Builder coordinate(LinearRing linearRing);

        Builder coordinates(Collection<? extends LinearRing> linearRings);

        Builder clearCoordinates();
    }

    private static class BuilderImpl extends GeoJSON.BuilderImpl<Polygon, Builder> implements Builder {
        private List<LinearRing> coordinates;

        @Override
        public Builder coordinate(LinearRing linearRing) {
            if (coordinates == null) coordinates = new ArrayList<>();
            coordinates.add(linearRing);
            return this;
        }

        @Override
        public Builder coordinates(Collection<? extends LinearRing> linearRings) {
            if (coordinates == null) coordinates = new ArrayList<>();
            coordinates.addAll(linearRings);
            return this;
        }

        @Override
        public Builder clearCoordinates() {
            this.coordinates = null;
            return this;
        }

        public Polygon build() {
            return new Polygon(getBbox(), getForeignMembers(), this.coordinates);
        }

        @Override
        public Polygon buildWithComputedBbox() {
            if (coordinates != null && !coordinates.isEmpty()) {
                bbox(GeoJSONUtil.computeBbox(flattenCoordinates(coordinates)));
            }
            return build();
        }
    }
}
