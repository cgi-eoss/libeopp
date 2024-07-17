/*
 *  Copyright 2022 The libeopp Team
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
// This file extends Protobuf's own Anies.kt, with copyright:
//
// Protocol Buffers - Google's data interchange format
// Copyright 2008 Google Inc.  All rights reserved.
// https://developers.google.com/protocol-buffers/
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are
// met:
//
//     * Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer.
//     * Redistributions in binary form must reproduce the above
// copyright notice, this list of conditions and the following disclaimer
// in the documentation and/or other materials provided with the
// distribution.
//     * Neither the name of Google Inc. nor the names of its
// contributors may be used to endorse or promote products derived from
// this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
// LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
// A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
// OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
// SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
// LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
// DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
// THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
// OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
package com.cgi.eoss.eopp.util

import com.google.protobuf.AnyKt
import com.google.protobuf.BoolValue
import com.google.protobuf.ByteString
import com.google.protobuf.BytesValue
import com.google.protobuf.Int32Value
import com.google.protobuf.Int64Value
import com.google.protobuf.Message
import com.google.protobuf.StringValue
import com.google.protobuf.UInt32Value
import com.google.protobuf.UInt64Value
import java.util.Optional
import com.google.protobuf.Any as ProtoAny

/** @return `true` if this [com.google.protobuf.Any] contains a message of type `T`. */
inline fun <reified T : Message> ProtoAny.isA(): Boolean = this.`is`(T::class.java)

/**
 * @return the message of type `T` encoded in this [com.google.protobuf.Any].
 *
 * @throws com.google.protobuf.InvalidProtocolBufferException if this [com.google.protobuf.Any] does not contain a `T`
 * message.
 */
inline fun <reified T : Message> ProtoAny.unpack(): T = unpack(T::class.java)

/**
 * @return A proto [Any] packing this [Message].
 */
inline fun <reified T : Message> T.toAny(): ProtoAny = ProtoAny.pack(this)

/**
 * @return the message of type `T` encoded in this [com.google.protobuf.Any].
 *
 * @throws com.google.protobuf.InvalidProtocolBufferException if this [com.google.protobuf.Any] does not contain a `T`
 * message.
 */
fun <T : Message> unpack(any: ProtoAny, clazz: Class<T>): T = any.unpack(clazz)

/**
 * @return an Optional containing the message of type `T` encoded in this [com.google.protobuf.Any], or an empty
 * Optional if this [com.google.protobuf.Any] does not contain a `T` message.
 */
fun <T : Message> safeUnpack(any: ProtoAny, clazz: Class<T>): Optional<T> =
    if (any.`is`(clazz)) Optional.of(any.unpack(clazz)) else Optional.empty()

/**
 * @return A proto [Any] packing the given [String] in a [StringValue].
 */
fun from(value: String) = StringValue.of(value).toAny()

/**
 * @return A proto [Any] packing the given [Int] in an [Int32Value].
 */
fun from(value: Int) = Int32Value.of(value).toAny()

/**
 * @return A proto [Any] packing the given [Int] in a [UInt32Value].
 */
fun unsignedFrom(value: Int) = UInt32Value.of(value).toAny()

/**
 * @return A proto [Any] packing the given [Long] in an [Int64Value].
 */
fun from(value: Long) = Int64Value.of(value).toAny()

/**
 * @return A proto [Any] packing the given [Long] in a [UInt64Value].
 */
fun unsignedFrom(value: Long) = UInt64Value.of(value).toAny()

/**
 * @return A proto [Any] packing the given [Boolean] in a [BoolValue].
 */
fun from(value: Boolean) = BoolValue.of(value).toAny()

/**
 * @return A proto [Any] packing the given [ByteArray] in a [BytesValue].
 */
fun from(value: ByteArray) = BytesValue.of(ByteString.copyFrom(value)).toAny()
