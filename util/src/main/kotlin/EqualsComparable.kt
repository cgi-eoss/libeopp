/*
 * Copyright 2024 The libeopp Team
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
package com.cgi.eoss.eopp.util

/**
 * @return true if this object is equal to the other per their comparison, i.e. `this.compareTo(other)` returns 0.
 */
fun <T : Comparable<T>> T.equalsComparable(other: T): Boolean = this.compareTo(other) == 0

/**
 * @return true if all objects in this list are equal to the other per their comparison in order, i.e. if for each index
 * `i`, `this.get(i).compareTo(other.get(i))` returns 0.
 */
fun <C : List<T>, T : Comparable<T>> C.equalsComparable(other: C): Boolean {
    if (this.size != other.size) return false
    return asSequence().mapIndexed { i, it -> it.equalsComparable(other[i]) }.all { it }
}
