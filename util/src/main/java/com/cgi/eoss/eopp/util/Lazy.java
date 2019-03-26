package com.cgi.eoss.eopp.util;

import java.util.Optional;
import java.util.function.Supplier;

public final class Lazy {
    private Lazy() {
        // no-op utility class constructor
    }

    public static <Z> Supplier<Z> lazily(Supplier<Z> supplier) {
        return new Supplier<Z>() {
            Z value;

            @Override
            public Z get() {
                return Optional.ofNullable(value).orElseGet(() -> value = supplier.get());
            }
        };
    }
}
