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

import com.fasterxml.jackson.annotation.JsonProperty;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

/**
 * <p>Abstract type representing the available types of GeoJSON Geometry objects: points, curves and surfaces in
 * coordinate space. Every Geometry object is a GeoJSON object no matter where it occurs in a GeoJSON text.</p>
 * <p>The subclasses of this type all expose their coordinates ultimately with each element represented by a
 * {@link Position} object.</p>
 *
 * @param <T> The coordinate structure of the geometry, e.g. a GeoJSON {@link Point} is represented by a single {@link Position}.
 * @param <J> The JTS geometry type, e.g. a GeoJSON {@link Polygon} corresponds to {@link org.locationtech.jts.geom.Polygon}.
 * @see <a href="https://tools.ietf.org/html/rfc7946#section-3.1">https://tools.ietf.org/html/rfc7946#section-3.1</a>
 */
public abstract class Geometry<T, J extends org.locationtech.jts.geom.Geometry> extends GeoJSON implements JtsGeometry<J> {
    protected static final GeometryFactory DEFAULT_GEOMETRY_FACTORY = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING));

    private static final EnumSet<GeoJSONType> GEOMETRY_TYPES = EnumSet.of(
            GeoJSONType.Point,
            GeoJSONType.MultiPoint,
            GeoJSONType.LineString,
            GeoJSONType.MultiLineString,
            GeoJSONType.Polygon,
            GeoJSONType.MultiPolygon,
            GeoJSONType.GeometryCollection
    );

    Geometry(GeoJSONType type, List<BigDecimal> bbox, Map<String, Object> foreignMembers) {
        super(type, bbox, foreignMembers);

        if (!GEOMETRY_TYPES.contains(getType())) {
            throw new IllegalArgumentException(String.format("GeoJSON type %s is not a valid Geometry type", getType()));
        }
    }

    @JsonProperty("coordinates")
    public abstract T getCoordinates();

    public abstract List<Position> computeFlattenedCoordinates();

    /**
     * @return An array describing the boundary extents of this geometry: all axes of the most southwesterly point,
     * followed by all axes of the most northeasterly point.
     */
    public List<BigDecimal> computeBbox() {
        return GeoJSONUtil.computeBbox(computeFlattenedCoordinates());
    }
}