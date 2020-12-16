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

package com.cgi.eoss.eopp.util;

import com.google.protobuf.Timestamp;

import java.time.Instant;

/**
 * <p>Utility methods for working with {@link Timestamp} objects.</p>
 */
public final class Timestamps {

    private Timestamps() {
        // no-op for utility class
    }

    /**
     * @return A proto {@link Timestamp} representing the given {@link Instant}.
     */
    public static Timestamp timestampFromInstant(Instant instant) {
        return Timestamp.newBuilder().setSeconds(instant.getEpochSecond()).setNanos(instant.getNano()).build();
    }

    /**
     * @return An {@link Instant} representing the given {@link Timestamp}.
     */
    public static Instant instantFromTimestamp(Timestamp timestamp) {
        return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
    }
}
