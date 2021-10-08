/*
 * Copyright 2021 The libeopp Team
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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * <p>A {@link CountDownLatch} which may be manually aborted with a {@link Throwable} cause, allowing the caller to
 * catch and explicitly handle the wrapping {@link AbortedException}.</p>
 */
public class AbortableCountDownLatch extends CountDownLatch {

    private boolean aborted;
    private String message;
    private Throwable cause;

    /**
     * Constructs an {@code AbortableCountDownLatch} initialized with the given count.
     *
     * @param count the number of times {@link #countDown} must be invoked
     *              before threads can pass through {@link #await}
     * @throws IllegalArgumentException if {@code count} is negative
     */
    public AbortableCountDownLatch(int count) {
        super(count);
    }

    /**
     * <p>Causes all still-waiting clients of this latch to interrupt with the given message and cause. If this latch
     * has already counted down to zero, no action is taken.</p>
     *
     * @param message A description of the cause of the abort of this latch.
     * @param cause   The underlying cause of the abort of this latch.
     */
    public void abort(String message, Throwable cause) {
        // We've already counted down, no one is listening
        if (getCount() == 0) {
            return;
        }

        // Set the flag and abort details
        this.aborted = true;
        this.message = message;
        this.cause = cause;

        // Spin down the counter
        while (getCount() > 0) {
            countDown();
        }
    }

    @Override
    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        final boolean countedDown = super.await(timeout, unit);
        if (aborted) {
            throw new AbortedException(message, cause);
        }
        return countedDown;
    }

    @Override
    public void await() throws InterruptedException {
        super.await();
        if (aborted) {
            throw new AbortedException(message, cause);
        }
    }

}