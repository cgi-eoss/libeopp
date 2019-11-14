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
