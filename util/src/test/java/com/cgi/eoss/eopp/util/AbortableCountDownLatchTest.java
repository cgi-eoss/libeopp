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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class AbortableCountDownLatchTest {

    @Test
    public void testAwait() throws InterruptedException {
        AbortableCountDownLatch latch = new AbortableCountDownLatch(2);

        AtomicBoolean bool = new AtomicBoolean(false);
        CompletableFuture.runAsync(() -> {
            bool.set(true);
            latch.countDown();
            latch.countDown();
        });

        latch.await();

        assertThat(bool.get()).isTrue();
    }

    @Test
    public void testAwaitWithTimeout() throws InterruptedException {
        AbortableCountDownLatch latch = new AbortableCountDownLatch(1);

        AtomicBoolean bool = new AtomicBoolean(false);
        CompletableFuture.runAsync(() -> {
            bool.set(true);
            try {
                Thread.sleep(Duration.ofSeconds(1).toMillis());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            latch.countDown();
        });

        boolean countedDown = latch.await(100, TimeUnit.MILLISECONDS);

        assertThat(countedDown).isFalse();
        assertThat(bool.get()).isTrue();
    }

    @Test
    public void testAbort() throws InterruptedException {
        AbortableCountDownLatch latch = new AbortableCountDownLatch(2);

        CompletableFuture.runAsync(() -> {
            latch.abort("Failed in async code", new IllegalStateException("IllegalStateException"));
        });

        try {
            latch.await();
            fail("Expected AbortedException");
        } catch (AbortedException e) {
            assertThat(e).hasMessageThat().isEqualTo("Failed in async code");
            assertThat(e).hasCauseThat().isInstanceOf(IllegalStateException.class);
            assertThat(e).hasCauseThat().hasMessageThat().isEqualTo("IllegalStateException");
        }
    }

    @Test
    public void testAbortWithTimeout() throws InterruptedException {
        AbortableCountDownLatch latch = new AbortableCountDownLatch(1);

        CompletableFuture.runAsync(() -> {
            latch.abort("Failed in async code", new IllegalStateException("IllegalStateException"));
        });

        try {
            boolean countedDown = latch.await(1, TimeUnit.SECONDS);
            fail("Expected AbortedException");
        } catch (AbortedException e) {
            assertThat(e).hasMessageThat().isEqualTo("Failed in async code");
            assertThat(e).hasCauseThat().isInstanceOf(IllegalStateException.class);
            assertThat(e).hasCauseThat().hasMessageThat().isEqualTo("IllegalStateException");
        }
    }

    @Test
    public void testAbortAfterCountDown() throws InterruptedException {
        AbortableCountDownLatch latch = new AbortableCountDownLatch(1);

        AtomicBoolean bool = new AtomicBoolean(false);
        CompletableFuture.runAsync(() -> {
            bool.set(true);
            latch.countDown();
            latch.abort("Failed in async code", new IllegalStateException("IllegalStateException"));
        });

        latch.await();

        assertThat(bool.get()).isTrue();
    }

}