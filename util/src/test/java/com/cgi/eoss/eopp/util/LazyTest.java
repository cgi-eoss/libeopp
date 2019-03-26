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