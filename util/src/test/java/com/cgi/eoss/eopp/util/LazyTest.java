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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static com.google.common.truth.Truth.assertThat;

@RunWith(JUnit4.class)
public class LazyTest {

    @Test
    public void testLazy() {
        String result = "result";

        AtomicInteger count = new AtomicInteger(0);
        Supplier<String> countingSupplier = () -> {
            count.addAndGet(1);
            return result;
        };

        Supplier<String> lazyResult = Lazy.lazily(countingSupplier);

        assertThat(count.get()).isEqualTo(0);
        assertThat(lazyResult.get()).isEqualTo(result);
        assertThat(count.get()).isEqualTo(1);
        assertThat(lazyResult.get()).isEqualTo(result);
        assertThat(count.get()).isEqualTo(1);
    }

}