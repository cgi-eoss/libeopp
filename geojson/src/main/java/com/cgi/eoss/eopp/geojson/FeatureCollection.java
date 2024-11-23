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

/**
 * <p>A GeoJSON object comprising zero or more {@link Feature} objects.</p>
 *
 * @see <a href="https://tools.ietf.org/html/rfc7946#section-3.3">https://tools.ietf.org/html/rfc7946#section-3.3</a>
 */
@JsonPropertyOrder({"type", "features", "bbox"})
public class FeatureCollection extends GeoJSON {

    private final List<Feature> features;

    @JsonCreator
    public FeatureCollection(
            @JsonProperty("bbox") List<BigDecimal> bbox,
            @JsonAnySetter @JsonProperty("foreignMembers") Map<String, Object> foreignMembers,
            @JsonProperty("features") List<Feature> features) {
        super(GeoJSONType.FeatureCollection, bbox, foreignMembers);

        this.features = Optional.ofNullable(features).map(Collections::unmodifiableList)
                .orElseThrow(() -> new IllegalArgumentException("FeatureCollection is missing required 'features' property"));
    }

    public static Builder builder() {
        return new BuilderImpl();
    }

    @JsonProperty("features")
    public List<Feature> getFeatures() {
        return features;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), features);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FeatureCollection that = (FeatureCollection) o;
        return Objects.equals(features, that.features);
    }

    @Override
    public String toString() {
        return "FeatureCollection{" +
               "features=" + features +
               toStringProperties() +
               '}';
    }

    public Builder toBuilder() {
        return new BuilderImpl()
                .bbox(getBbox())
                .foreignMembers(getForeignMembers())
                .features(this.features);
    }

    public interface Builder extends GeoJSON.Builder<FeatureCollection, Builder> {
        Builder feature(Feature feature);

        Builder features(Collection<? extends Feature> features);

        Builder clearFeatures();
    }

    private static class BuilderImpl extends GeoJSON.BuilderImpl<FeatureCollection, Builder> implements Builder {
        private List<Feature> features;

        @Override
        public Builder feature(Feature feature) {
            if (this.features == null) this.features = new ArrayList<>();
            this.features.add(feature);
            return this;
        }

        @Override
        public Builder features(Collection<? extends Feature> features) {
            if (this.features == null) this.features = new ArrayList<>();
            this.features.addAll(features);
            return this;
        }

        @Override
        public Builder clearFeatures() {
            this.features = null;
            return this;
        }

        public FeatureCollection build() {
            return new FeatureCollection(getBbox(), getForeignMembers(), features);
        }

        @Override
        public FeatureCollection buildWithComputedBbox() {
            if (features != null && !features.isEmpty()) {
                bbox(GeoJSONUtil.computeBbox(features.stream().map(Feature::getGeometry).filter(Objects::nonNull).flatMap(geometry -> geometry.computeFlattenedCoordinates().stream())));
            }
            return build();
        }
    }
}
