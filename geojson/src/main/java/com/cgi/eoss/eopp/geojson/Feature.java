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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * <p>A GeoJSON object representing a spatially bounded thing.</p>
 *
 * @see <a href="https://tools.ietf.org/html/rfc7946#section-3.2">https://tools.ietf.org/html/rfc7946#section-3.2</a>
 */
@JsonPropertyOrder({"type", "id", "properties", "geometry", "bbox"})
public class Feature extends GeoJSON {

    private final Object id;

    private final Geometry<?, ? extends org.locationtech.jts.geom.Geometry> geometry;

    private final Map<String, Object> properties;

    @JsonCreator
    public Feature(
            @JsonProperty("bbox") List<BigDecimal> bbox,
            @JsonAnySetter @JsonProperty("foreignMembers") Map<String, Object> foreignMembers,
            @JsonProperty("id") Object id,
            @JsonProperty("geometry") Geometry<?, ? extends org.locationtech.jts.geom.Geometry> geometry,
            @JsonProperty("properties") Map<String, Object> properties) {
        super(GeoJSONType.Feature, bbox, foreignMembers);

        if (id != null && !(id instanceof Number || id instanceof String)) {
            throw new IllegalArgumentException(String.format("A Feature's \"id\" must be a JSON string or number, but \"%s\" is a %s", id, id.getClass().getCanonicalName()));
        }

        this.id = id;
        this.geometry = geometry;
        this.properties = Optional.ofNullable(properties).map(Collections::unmodifiableMap).orElse(null);

        // RFC7946: Any geometry that crosses the antimeridian SHOULD be
        // represented by cutting it in two such that neither part's
        // representation crosses the antimeridian.
        //TODO Detect antimeridian crossing and modify geometry
    }

    public static Builder builder() {
        return new BuilderImpl();
    }

    @JsonProperty("id")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Nullable
    public Object getId() {
        return id;
    }

    @JsonProperty("geometry")
    @Nullable
    public Geometry<?, ? extends org.locationtech.jts.geom.Geometry> getGeometry() {
        return geometry;
    }

    @JsonProperty("properties")
    @Nullable
    public Map<String, Object> getProperties() {
        return properties;
    }

    @JsonIgnore
    public Set<String> getPropertyKeys() {
        return properties != null ? properties.keySet() : Collections.emptySet();
    }

    @JsonIgnore
    public String getPropertyAsString(String key) {
        return properties != null ? String.valueOf(properties.get(key)) : null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, geometry, properties);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Feature feature = (Feature) o;
        return Objects.equals(id, feature.id) &&
               Objects.equals(geometry, feature.geometry) &&
               Objects.equals(properties, feature.properties);
    }

    @Override
    public String toString() {
        return "Feature{" +
               "id=" + id +
               ", properties=" + properties +
               ", geometry=" + geometry +
               toStringProperties() +
               '}';
    }

    public Builder toBuilder() {
        Builder builder = new BuilderImpl()
                .bbox(this.getBbox())
                .foreignMembers(this.getForeignMembers())
                .id(this.id)
                .geometry(this.geometry);
        Optional.ofNullable(this.properties).ifPresent(builder::properties);
        return builder;
    }

    public interface Builder extends GeoJSON.Builder<Feature, Builder> {
        Builder id(Object id);

        Builder geometry(Geometry<?, ? extends org.locationtech.jts.geom.Geometry> geometry);

        Builder property(String key, Object value);

        Builder properties(Map<? extends String, ?> properties);

        Builder clearProperties();
    }

    protected static class BuilderImpl extends GeoJSON.BuilderImpl<Feature, Builder> implements Builder {
        private Object id;
        private Geometry<?, ? extends org.locationtech.jts.geom.Geometry> geometry;
        private List<String> propertiesKeys;
        private List<Object> propertiesValues;

        @Override
        public Builder id(Object id) {
            this.id = id;
            return this;
        }

        @Override
        public Builder geometry(Geometry<?, ? extends org.locationtech.jts.geom.Geometry> geometry) {
            this.geometry = geometry;
            return this;
        }

        @Override
        public Builder property(String key, Object value) {
            if (this.propertiesKeys == null) {
                this.propertiesKeys = new ArrayList<>();
                this.propertiesValues = new ArrayList<>();
            }
            this.propertiesKeys.add(key);
            this.propertiesValues.add(value);
            return this;
        }

        @Override
        public Builder properties(Map<? extends String, ?> properties) {
            if (this.propertiesKeys == null) {
                this.propertiesKeys = new ArrayList<>();
                this.propertiesValues = new ArrayList<>();
            }
            for (final Map.Entry<? extends String, ?> entry : properties.entrySet()) {
                this.propertiesKeys.add(entry.getKey());
                this.propertiesValues.add(entry.getValue());
            }
            return this;
        }

        @Override
        public Builder clearProperties() {
            if (this.propertiesKeys != null) {
                this.propertiesKeys.clear();
                this.propertiesValues.clear();
            }
            return this;
        }

        @Override
        public Feature build() {
            return new Feature(getBbox(), getForeignMembers(), id, geometry, getProperties());
        }

        @Override
        public Feature buildWithComputedBbox() {
            if (geometry != null) {
                bbox(Optional.ofNullable(geometry.getBbox()).orElseGet(geometry::computeBbox));
            }
            return build();
        }

        protected Object getId() {
            return id;
        }

        protected Geometry<?, ? extends org.locationtech.jts.geom.Geometry> getGeometry() {
            return geometry;
        }

        protected Map<String, Object> getProperties() {
            Map<String, Object> properties;
            if (this.propertiesKeys == null) {
                properties = null;
            } else {
                switch (this.propertiesKeys.size()) {
                    case 0:
                        properties = Collections.emptyMap();
                        break;
                    case 1:
                        properties = Collections.singletonMap(this.propertiesKeys.get(0), this.propertiesValues.get(0));
                        break;
                    default:
                        properties = new LinkedHashMap<>(this.propertiesKeys.size() < 1073741824 ? 1 + this.propertiesKeys.size() + (this.propertiesKeys.size() - 3) / 3 : Integer.MAX_VALUE);
                        for (int i = 0; i < this.propertiesKeys.size(); i++)
                            properties.put(this.propertiesKeys.get(i), this.propertiesValues.get(i));
                        properties = Collections.unmodifiableMap(properties);
                }
            }
            return properties;
        }
    }
}
