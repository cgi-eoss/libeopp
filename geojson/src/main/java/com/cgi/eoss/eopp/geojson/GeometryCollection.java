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
import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * <p>A collection of zero or more {@link Geometry} objects.</p>
 *
 * @see <a href="https://tools.ietf.org/html/rfc7946#section-3.1.8">https://tools.ietf.org/html/rfc7946#section-3.1.8</a>
 */
@JsonPropertyOrder({"type", "geometries", "bbox"})
public class GeometryCollection extends Geometry<Object, org.locationtech.jts.geom.GeometryCollection> {

    private final List<Geometry<?, ? extends org.locationtech.jts.geom.Geometry>> geometries;

    @JsonCreator
    public GeometryCollection(
            @JsonProperty("bbox") List<BigDecimal> bbox,
            @JsonProperty("foreignMembers") Map<String, Object> foreignMembers,
            @JsonProperty("geometries") List<Geometry<?, ? extends org.locationtech.jts.geom.Geometry>> geometries) {
        super(GeoJSONType.GeometryCollection, bbox, foreignMembers);

        this.geometries = Optional.ofNullable(geometries).map(Collections::unmodifiableList)
                .orElseThrow(() -> new IllegalArgumentException("GeometryCollection is missing required 'geometries' property"));
    }

    private static Stream<Position> flattenCoordinates(List<Geometry<?, ? extends org.locationtech.jts.geom.Geometry>> geometries) {
        return geometries.stream().flatMap(geometry -> geometry.computeFlattenedCoordinates().stream());
    }

    public static Builder builder() {
        return new BuilderImpl();
    }

    @JsonProperty("geometries")
    public List<Geometry<?, ? extends org.locationtech.jts.geom.Geometry>> getGeometries() {
        return geometries;
    }

    @JsonIgnore
    @Override
    public List<Object> getCoordinates() {
        return geometries.stream().map(Geometry::getCoordinates).collect(toList());
    }

    @Override
    public List<Position> computeFlattenedCoordinates() {
        return flattenCoordinates(geometries).collect(toList());
    }

    @Override
    public org.locationtech.jts.geom.GeometryCollection toJts() {
        return new org.locationtech.jts.geom.GeometryCollection(
                geometries.stream().map(Geometry::toJts).toArray(org.locationtech.jts.geom.Geometry[]::new),
                DEFAULT_GEOMETRY_FACTORY
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), geometries);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GeometryCollection that = (GeometryCollection) o;
        return Objects.equals(geometries, that.geometries);
    }

    @Override
    public String toString() {
        return "GeometryCollection{" +
                "geometries=" + geometries +
                toStringProperties() +
                '}';
    }

    public Builder toBuilder() {
        return new BuilderImpl()
                .bbox(this.getBbox())
                .foreignMembers(this.getForeignMembers())
                .geometries(this.geometries);
    }

    public interface Builder extends GeoJSON.Builder<GeometryCollection, Builder> {
        Builder geometry(Geometry<?, ? extends org.locationtech.jts.geom.Geometry> geometry);

        Builder geometries(Collection<Geometry<?, ? extends org.locationtech.jts.geom.Geometry>> geometries);

        Builder clearGeometries();
    }

    private static class BuilderImpl extends GeoJSON.BuilderImpl<GeometryCollection, Builder> implements Builder {
        private List<Geometry<?, ? extends org.locationtech.jts.geom.Geometry>> geometries;

        @Override
        public Builder geometry(Geometry<?, ? extends org.locationtech.jts.geom.Geometry> geometry) {
            if (this.geometries == null) this.geometries = new ArrayList<>();
            this.geometries.add(geometry);
            return this;
        }

        @Override
        public Builder geometries(Collection<Geometry<?, ? extends org.locationtech.jts.geom.Geometry>> geometries) {
            if (this.geometries == null) this.geometries = new ArrayList<>();
            this.geometries.addAll(geometries);
            return this;
        }

        @Override
        public Builder clearGeometries() {
            this.geometries = null;
            return this;
        }

        @Override
        public GeometryCollection build() {
            return new GeometryCollection(getBbox(), getForeignMembers(), geometries);
        }

        @Override
        public GeometryCollection buildWithComputedBbox() {
            if (geometries != null && !geometries.isEmpty()) {
                bbox(GeoJSONUtil.computeBbox(flattenCoordinates(geometries)));
            }
            return build();
        }
    }
}
