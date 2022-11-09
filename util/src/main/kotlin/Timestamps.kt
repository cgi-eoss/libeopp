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
package com.cgi.eoss.eopp.util

import com.google.protobuf.Timestamp
import com.google.protobuf.timestamp
import java.time.Instant

/**
 * Utility methods for working with [Timestamp] objects.
 */
object Timestamps {
    /**
     * @return A proto [Timestamp] representing the given [Instant].
     */
    @JvmStatic
    fun timestampFromInstant(instant: Instant): Timestamp = instant.toTimestamp()

    /**
     * @return An [Instant] representing the given [Timestamp].
     */
    @JvmStatic
    fun instantFromTimestamp(timestamp: Timestamp): Instant = timestamp.toInstant()
}

/**
 * @return A proto [Timestamp] representing this [Instant].
 */
fun Instant.toTimestamp(): Timestamp = timestamp { seconds = epochSecond; nanos = nano }

/**
 * @return An [Instant] representing this [Timestamp].
 */
fun Timestamp.toInstant(): Instant = Instant.ofEpochSecond(seconds, nanos.toLong())
