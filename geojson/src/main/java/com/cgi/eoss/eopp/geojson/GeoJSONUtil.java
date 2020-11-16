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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public final class GeoJSONUtil {
    /**
     * IANA media type for GeoJSON text.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7946#section-12">https://tools.ietf.org/html/rfc7946#section-12</a>
     */
    public static final String GEOJSON_MEDIA_TYPE = "application/geo+json";

    private GeoJSONUtil() {
        // no-op
    }

    /**
     * <p>Find the minimum and maximum boundaries of each coordinate represented in the given collection of
     * {@link Position}s.</p>
     *
     * @return All axes of the most southwesterly point, followed by all axes of the most northeasterly point.
     */
    public static List<BigDecimal> computeBbox(Iterable<Position> coordinates) {
        BigDecimal[] min = new BigDecimal[]{null, null, null};
        BigDecimal[] max = new BigDecimal[]{null, null, null};

        for (Position coordinate : coordinates) {
            min[0] = min[0] != null ? min[0].min(coordinate.getLongitude()) : coordinate.getLongitude();
            max[0] = max[0] != null ? max[0].max(coordinate.getLongitude()) : coordinate.getLongitude();

            min[1] = min[1] != null ? min[1].min(coordinate.getLatitude()) : coordinate.getLatitude();
            max[1] = max[1] != null ? max[1].max(coordinate.getLatitude()) : coordinate.getLatitude();

            Optional.ofNullable(coordinate.getAltitude()).ifPresent(altitude -> {
                min[2] = min[2] != null ? min[2].min(altitude) : altitude;
                max[2] = max[2] != null ? max[2].max(altitude) : altitude;
            });
        }

        List<BigDecimal> bboxCoordinates = new ArrayList<>();
        bboxCoordinates.add(min[0]);
        bboxCoordinates.add(min[1]);
        Optional.ofNullable(min[2]).ifPresent(bboxCoordinates::add);
        bboxCoordinates.add(max[0]);
        bboxCoordinates.add(max[1]);
        Optional.ofNullable(max[2]).ifPresent(bboxCoordinates::add);
        return bboxCoordinates;
    }

    /**
     * <p>Find the minimum and maximum boundaries of each coordinate represented in the given collection of
     * {@link Position}s.</p>
     *
     * @return All axes of the most southwesterly point, followed by all axes of the most northeasterly point.
     */
    public static List<BigDecimal> computeBbox(Stream<Position> stream) {
        return computeBbox(stream::iterator);
    }
}
