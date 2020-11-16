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
 * <p>A Geometry construct comprising multiple {@link LineString} coordinate arrays.</p>
 *
 * @see <a href="https://tools.ietf.org/html/rfc7946#section-3.1.5">https://tools.ietf.org/html/rfc7946#section-3.1.5</a>
 */
@JsonPropertyOrder({"type", "coordinates"})
public class MultiLineString extends Geometry<List<List<Position>>, org.locationtech.jts.geom.MultiLineString> {

    private final List<List<Position>> coordinates;

    @JsonCreator
    public MultiLineString(
            @JsonProperty("bbox") List<BigDecimal> bbox,
            @JsonProperty("foreignMembers") Map<String, Object> foreignMembers,
            @JsonProperty("coordinates") List<List<Position>> coordinates) {
        super(GeoJSONType.MultiLineString, bbox, foreignMembers);

        this.coordinates = Optional.ofNullable(coordinates).map(it -> Collections.unmodifiableList(it.stream().map(Collections::unmodifiableList).collect(toList())))
                .orElseThrow(() -> new IllegalArgumentException("MultiLineString is missing required 'coordinates' property"));
    }

    public static Builder builder() {
        return new BuilderImpl();
    }

    public static MultiLineString fromLineStrings(List<LineString> lineStrings) {
        return new BuilderImpl().lineStrings(lineStrings).build();
    }

    private static Stream<Position> flattenCoordinates(List<List<Position>> coordinates) {
        return coordinates.stream().flatMap(Collection::stream);
    }

    public List<LineString> toLineStrings() {
        return coordinates.stream().map(it -> new LineString(null, null, it)).collect(toList());
    }

    @Override
    public List<List<Position>> getCoordinates() {
        return coordinates;
    }

    @Override
    public List<Position> computeFlattenedCoordinates() {
        return flattenCoordinates(coordinates).collect(toList());
    }

    @Override
    public org.locationtech.jts.geom.MultiLineString toJts() {
        return DEFAULT_GEOMETRY_FACTORY.createMultiLineString(
                toLineStrings().stream().map(LineString::toJts).toArray(org.locationtech.jts.geom.LineString[]::new)
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
        MultiLineString that = (MultiLineString) o;
        return Objects.equals(coordinates, that.coordinates);
    }

    @Override
    public String toString() {
        return "MultiLineString{" +
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

    public interface Builder extends GeoJSON.Builder<MultiLineString, Builder> {
        Builder coordinate(List<Position> coordinate);

        Builder lineString(LineString lineString);

        Builder coordinates(Collection<? extends List<Position>> coordinates);

        Builder lineStrings(Collection<? extends LineString> lineStrings);

        Builder clearCoordinates();
    }

    public static class BuilderImpl extends GeoJSON.BuilderImpl<MultiLineString, Builder> implements Builder {
        private List<List<Position>> coordinates;

        @Override
        public BuilderImpl coordinate(List<Position> coordinate) {
            if (this.coordinates == null) this.coordinates = new ArrayList<>();
            this.coordinates.add(coordinate);
            return this;
        }

        @Override
        public Builder lineString(LineString lineString) {
            if (this.coordinates == null) this.coordinates = new ArrayList<>();
            this.coordinates.add(lineString.getCoordinates());
            return this;
        }

        @Override
        public Builder coordinates(Collection<? extends List<Position>> coordinates) {
            if (this.coordinates == null) this.coordinates = new ArrayList<>();
            this.coordinates.addAll(coordinates);
            return this;
        }

        @Override
        public Builder lineStrings(Collection<? extends LineString> lineStrings) {
            if (this.coordinates == null) this.coordinates = new ArrayList<>();
            lineStrings.forEach(this::lineString);
            return this;
        }

        @Override
        public Builder clearCoordinates() {
            this.coordinates = null;
            return this;
        }

        public MultiLineString build() {
            return new MultiLineString(getBbox(), getForeignMembers(), this.coordinates);
        }

        @Override
        public MultiLineString buildWithComputedBbox() {
            if (coordinates != null && !coordinates.isEmpty()) {
                bbox(GeoJSONUtil.computeBbox(flattenCoordinates(coordinates)));
            }
            return build();
        }
    }
}
