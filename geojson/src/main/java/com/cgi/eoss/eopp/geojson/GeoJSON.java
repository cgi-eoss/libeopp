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

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * <p>Abstract type representing the available types of GeoJSON object: Geometry, Feature or collection of Features.</p>
 *
 * @see <a href="https://tools.ietf.org/html/rfc7946">https://tools.ietf.org/html/rfc7946</a>
 * @see <a href="https://tools.ietf.org/html/rfc7946#section-3">https://tools.ietf.org/html/rfc7946#section-3</a>
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(Point.class),
        @JsonSubTypes.Type(MultiPoint.class),
        @JsonSubTypes.Type(LineString.class),
        @JsonSubTypes.Type(MultiLineString.class),
        @JsonSubTypes.Type(Polygon.class),
        @JsonSubTypes.Type(MultiPolygon.class),
        @JsonSubTypes.Type(GeometryCollection.class),
        @JsonSubTypes.Type(Feature.class),
        @JsonSubTypes.Type(FeatureCollection.class),
})
public abstract class GeoJSON {

    private final GeoJSONType type;

    private final List<BigDecimal> bbox;

    private final Map<String, Object> foreignMembers;

    protected GeoJSON(GeoJSONType type, List<BigDecimal> bbox, Map<String, Object> foreignMembers) {
        if (bbox != null && bbox.size() < 4) {
            // TODO The minimum size is 4, but we don't actually check the 2*n constraint against the contained geometries
            throw new IllegalArgumentException(String.format("The 'bbox' property must contain 2*n elements, where n is the number of dimensions represented in the contained geometries, but found %s", bbox.size()));
        }

        this.type = type;
        this.bbox = Optional.ofNullable(bbox).map(Collections::unmodifiableList).orElse(null);
        this.foreignMembers = Optional.ofNullable(foreignMembers).map(Collections::unmodifiableMap)
                .orElse(new LinkedHashMap<>()); // We have to allow a mutable map here in case this was instantiated by Jackson, so it can inject via JsonAnySetter
    }

    @JsonProperty("type")
    public GeoJSONType getType() {
        return this.type;
    }

    @JsonProperty("bbox")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public List<BigDecimal> getBbox() {
        return this.bbox;
    }

    @JsonAnySetter
    public void addForeignMember(String key, Object value) {
        foreignMembers.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getForeignMembers() {
        return this.foreignMembers;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, bbox, foreignMembers);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeoJSON geoJSON = (GeoJSON) o;
        return type == geoJSON.type &&
                (bbox == null ? geoJSON.bbox == null : bbox.size() == geoJSON.bbox.size() && IntStream.range(0, bbox.size()).allMatch(i -> bbox.get(i).compareTo(geoJSON.bbox.get(i)) == 0)) &&
                Objects.equals(foreignMembers, geoJSON.foreignMembers);
    }

    @Override
    public String toString() {
        return "GeoJSON{" +
                "type=" + type +
                toStringProperties() +
                '}';
    }

    protected String toStringProperties() {
        return ", bbox=" + bbox +
                ", foreignMembers=" + foreignMembers;
    }

    public interface Builder<T extends GeoJSON, B extends Builder<T, B>> {
        B bbox(List<BigDecimal> bbox);

        B foreignMember(String key, Object value);

        B foreignMembers(Map<? extends String, ?> foreignMembers);

        B clearForeignMembers();

        T build();

        T buildWithComputedBbox();
    }

    protected abstract static class BuilderImpl<T extends GeoJSON, B extends Builder<T, B>> implements Builder<T, B> {
        private List<BigDecimal> bbox;
        private ArrayList<String> foreignMembersKeys;
        private ArrayList<Object> foreignMembersValues;

        @SuppressWarnings("unchecked")
        private B getThis() {
            return (B) this;
        }

        @Override
        public B bbox(List<BigDecimal> bbox) {
            this.bbox = bbox;
            return getThis();
        }

        @Override
        public B foreignMember(String foreignMemberKey, Object foreignMemberValue) {
            if (this.foreignMembersKeys == null) {
                this.foreignMembersKeys = new ArrayList<>();
                this.foreignMembersValues = new ArrayList<>();
            }
            this.foreignMembersKeys.add(foreignMemberKey);
            this.foreignMembersValues.add(foreignMemberValue);
            return getThis();
        }

        @Override
        public B foreignMembers(Map<? extends String, ?> foreignMembers) {
            if (this.foreignMembersKeys == null) {
                this.foreignMembersKeys = new ArrayList<>();
                this.foreignMembersValues = new ArrayList<>();
            }
            for (final Map.Entry<? extends String, ?> entry : foreignMembers.entrySet()) {
                this.foreignMembersKeys.add(entry.getKey());
                this.foreignMembersValues.add(entry.getValue());
            }
            return getThis();
        }

        @Override
        public B clearForeignMembers() {
            if (this.foreignMembersKeys != null) {
                this.foreignMembersKeys.clear();
                this.foreignMembersValues.clear();
            }
            return getThis();
        }

        protected List<BigDecimal> getBbox() {
            return bbox;
        }

        protected Map<String, Object> getForeignMembers() {
            Map<String, Object> foreignMembers;
            if (this.foreignMembersKeys == null) {
                return Collections.emptyMap();
            } else {
                switch (this.foreignMembersKeys.size()) {
                    case 0:
                        foreignMembers = Collections.emptyMap();
                        break;
                    case 1:
                        foreignMembers = Collections.singletonMap(this.foreignMembersKeys.get(0), this.foreignMembersValues.get(0));
                        break;
                    default:
                        foreignMembers = new LinkedHashMap<>(this.foreignMembersKeys.size() < 1073741824 ? 1 + this.foreignMembersKeys.size() + (this.foreignMembersKeys.size() - 3) / 3 : Integer.MAX_VALUE);
                        for (int i = 0; i < this.foreignMembersKeys.size(); i++)
                            foreignMembers.put(this.foreignMembersKeys.get(i), this.foreignMembersValues.get(i));
                        foreignMembers = Collections.unmodifiableMap(foreignMembers);
                }
            }
            return foreignMembers;
        }
    }
}
