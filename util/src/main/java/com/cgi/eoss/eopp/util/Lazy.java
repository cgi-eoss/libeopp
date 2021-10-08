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

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

public final class Lazy {
    private Lazy() {
        // no-op utility class constructor
    }

    /**
     * <p>Wrap the given {@link Supplier} for lazy, atomic execution. Ensures
     * the initialisation function is only called once, even if the resulting
     * Supplier is called multiple times and from multiple threads.</p>
     * <p>Note that the given Supplier must not return null.</p>
     *
     * @param supplier An initialisation function, which is evaluated only
     *                 once.
     * @param <Z>      The return type of the initialisation function.
     * @return A one-time lazy provider for the value produced by the given
     * Supplier.
     */
    public static <Z> Supplier<Z> lazily(Supplier<Z> supplier) {
        return new Supplier<>() {
            private final AtomicReference<Z> value = new AtomicReference<>();
            private final Lock lock = new ReentrantLock();

            @Override
            public Z get() {
                Z result = value.get();
                if (result == null) {
                    lock.lock();
                    try {
                        // Another thread may have entered between (result == null) and lock.lock(), so we check again
                        result = value.get();
                        return Objects.requireNonNullElseGet(
                                result,
                                () -> value.updateAndGet(x -> supplier.get()));
                    } finally {
                        lock.unlock();
                    }
                }
                return result;
            }
        };
    }
}
