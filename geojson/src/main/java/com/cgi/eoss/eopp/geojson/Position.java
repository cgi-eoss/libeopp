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
import com.fasterxml.jackson.annotation.JsonValue;
import org.locationtech.jts.geom.Coordinate;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * <p>The fundamental geometry construct in GeoJSON.</p>
 *
 * @see <a href="https://tools.ietf.org/html/rfc7946#section-3.1.1">https://tools.ietf.org/html/rfc7946#section-3.1.1</a>
 */
public final class Position {

    private final BigDecimal longitude;
    private final BigDecimal latitude;
    @Nullable
    private final BigDecimal altitude;

    public Position() {
        this(BigDecimal.ZERO, BigDecimal.ZERO, null);
    }

    public Position(double longitude, double latitude) {
        this(BigDecimal.valueOf(longitude), BigDecimal.valueOf(latitude));
    }

    public Position(double longitude, double latitude, double altitude) {
        this(BigDecimal.valueOf(longitude), BigDecimal.valueOf(latitude), Double.isNaN(altitude) ? null : BigDecimal.valueOf(altitude));
    }

    public Position(BigDecimal longitude, BigDecimal latitude) {
        this(longitude, latitude, null);
    }

    public Position(BigDecimal longitude, BigDecimal latitude, BigDecimal altitude) {
        if (longitude == null || latitude == null) {
            throw new IllegalArgumentException(String.format("A Position requires both longitude and latitude, received %s and %s", longitude, latitude));
        }
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
    }

    @JsonCreator
    private Position(BigDecimal[] coordinates) {
        // WARNING! We truncate GeoJSON coordinates since anything more than this is (probably...) improper precision in the source data
        this(coordinates[0].setScale(9, RoundingMode.HALF_EVEN),
                coordinates[1].setScale(9, RoundingMode.HALF_EVEN),
                coordinates.length == 3 && coordinates[2] != null ? coordinates[2].setScale(9, RoundingMode.HALF_EVEN) : null);
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonValue
    private BigDecimal[] toArray() {
        if (altitude == null) {
            return new BigDecimal[]{longitude, latitude};
        } else {
            return new BigDecimal[]{longitude, latitude, altitude};
        }
    }

    @JsonIgnore
    public int getDimensions() {
        return altitude == null ? 2 : 3;
    }

    @JsonIgnore
    public int getMaxPrecision() {
        return IntStream.of(longitude.scale(),
                latitude.scale(),
                altitude == null ? 0 : altitude.scale())
                .max()
                .orElseThrow(() -> new IllegalStateException("Cannot determine maximum precision"));
    }

    @JsonIgnore
    public BigDecimal[] getElements() {
        List<BigDecimal> elements = new ArrayList<>();
        elements.add(longitude);
        elements.add(latitude);
        if (altitude != null) {
            elements.add(altitude);
        }
        return elements.toArray(new BigDecimal[0]);
    }

    public BigDecimal getLongitude() {
        return this.longitude;
    }

    public BigDecimal getLatitude() {
        return this.latitude;
    }

    public BigDecimal getAltitude() {
        return this.altitude;
    }

    @Override
    public int hashCode() {
        return Objects.hash(longitude, latitude, altitude);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        // Compare BigDecimals (if both not null) numerically rather than by object equality
        if (longitude == null ? position.longitude != null : longitude.compareTo(position.longitude) != 0) return false;
        if (latitude == null ? position.latitude != null : latitude.compareTo(position.latitude) != 0) return false;
        if (altitude == null ? position.altitude != null : altitude.compareTo(position.altitude) != 0) return false;
        return true;
    }

    @Override
    public String toString() {
        return "Position{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                ", altitude=" + altitude +
                '}';
    }

    public Coordinate toJts() {
        return new Coordinate(longitude.doubleValue(),
                latitude.doubleValue(),
                altitude == null ? Double.NaN : altitude.doubleValue());
    }

    public Builder toBuilder() {
        return new Builder()
                .longitude(this.longitude)
                .latitude(this.latitude)
                .altitude(this.altitude);
    }

    public static class Builder {
        private BigDecimal longitude;
        private BigDecimal latitude;
        private BigDecimal altitude;

        public Builder longitude(BigDecimal longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder latitude(BigDecimal latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder altitude(BigDecimal altitude) {
            this.altitude = altitude;
            return this;
        }

        public Position build() {
            return new Position(longitude, latitude, altitude);
        }
    }
}
