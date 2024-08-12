package com.cgi.eoss.eopp.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.math.BigDecimal;
import java.util.List;

import static com.cgi.eoss.eopp.util.EqualsComparableKt.equalsComparable;
import static com.google.common.truth.Truth.assertThat;

@RunWith(JUnit4.class)
public class EqualsComparableTest {
    private final BigDecimal first = new BigDecimal("1.1");
    private final BigDecimal second = new BigDecimal("1.1000");
    private final BigDecimal wrong = new BigDecimal("1.10000000001");

    @Test
    public void testEqualsComparable() {
        assertThat(first.equals(second)).isFalse();
        assertThat(equalsComparable(first, second)).isTrue();
        assertThat(equalsComparable(first, wrong)).isFalse();
    }

    @Test
    public void testEqualsComparableList() {
        assertThat(equalsComparable(List.of(first, second), List.of(second, first))).isTrue();
        assertThat(equalsComparable(List.of(first), List.of(second, first))).isFalse();
        assertThat(equalsComparable(List.of(first, wrong), List.of(wrong, first))).isFalse();
    }
}
