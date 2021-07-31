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

package com.cgi.eoss.eopp.file;

import com.cgi.eoss.eopp.util.EoppHeaders;
import com.cgi.eoss.eopp.util.Timestamps;
import com.google.common.base.Strings;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.io.ByteSource;
import com.google.common.io.MoreFiles;
import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * <p>Utility methods for working with {@link FileMeta} objects.</p>
 */
public final class FileMetas {

    /**
     * <p>The default HashFunction used for calculating FileMeta checksum attributes.</p>
     * <p>Despite the caveats around md5's security, using this as the default checksum algorithm allows easier
     * interoperability with other services such as S3.</p>
     */
    @SuppressWarnings("deprecation")
    public static final HashFunction DEFAULT_HASH_FUNCTION = Hashing.md5();

    /**
     * <p>Checksum HashFunctions with their well-known prefix strings.</p>
     */
    @SuppressWarnings("deprecation")
    public static final BiMap<HashFunction, String> HASH_FUNCTIONS = ImmutableBiMap.<HashFunction, String>builder()
            .put(Hashing.md5(), "md5")
            .put(Hashing.murmur3_128(), "murmur3_128")
            .put(Hashing.sha256(), "sha256")
            .put(Hashing.sipHash24(), "sipHash24")
            .build();

    private FileMetas() {
        // no-op for utility class
    }

    /**
     * <p>Read the FileMeta from the given file.</p>
     * <p>The DEFAULT_HASH_FUNCTION is used to calculate the file checksum.</p>
     * <p>No additional metadata properties are added.</p>
     *
     * @param file The file to determine FileMeta from.
     * @return A complete FileMeta with the filename, size, checksum, executable and properties attributes set.
     * @see #get(Path, HashFunction, Map, boolean)
     */
    public static FileMeta get(Path file) {
        return get(file, DEFAULT_HASH_FUNCTION, Collections.emptyMap(), false);
    }

    /**
     * <p>Read the FileMeta from the given file.</p>
     * <p>The given HashFunction is used when calculating the file checksum.</p>
     * <p>No additional metadata properties are added.</p>
     *
     * @param file             The file to determine FileMeta from.
     * @param checksumFunction The HashFunction with which to calculate the file checksum.
     * @return A complete FileMeta with the filename, size, checksum, executable and properties attributes set.
     * @see #get(Path, HashFunction, Map, boolean)
     */
    public static FileMeta get(Path file, HashFunction checksumFunction) {
        return get(file, checksumFunction, Collections.emptyMap(), false);
    }

    /**
     * <p>Read the FileMeta from the given file.</p>
     * <p>The DEFAULT_HASH_FUNCTION is used to calculate the file checksum.</p>
     * <p>The given properties are added as additional metadata.</p>
     *
     * @param file       The file to determine FileMeta from.
     * @param properties Any additional FileMeta properties for use by receivers of the Message.
     * @return A complete FileMeta with the filename, size, checksum, executable and properties attributes set.
     * @see #get(Path, HashFunction, Map, boolean)
     */
    public static FileMeta get(Path file, Map<String, Any> properties) {
        return get(file, DEFAULT_HASH_FUNCTION, properties, false);
    }

    /**
     * <p>Read the FileMeta from the given file.</p>
     * <p>Extended filesystem attributes are used to find existing FileMeta stored with the file.</p>
     * <p>The given HashFunction is used when calculating the file checksum.</p>
     * <p>The given properties are added as additional metadata.</p>
     *
     * @param file             The file to determine FileMeta from.
     * @param checksumFunction The HashFunction with which to calculate the file checksum.
     * @param properties       Any additional FileMeta properties for use by receivers of the Message.
     * @param executable       Whether the file should be considered executable.
     * @return A complete FileMeta with the filename, size, checksum, executable and properties attributes set.
     */
    public static FileMeta get(Path file, HashFunction checksumFunction, Map<String, Any> properties, boolean executable) {
        try {
            FileMeta.Builder builder = FileMeta.newBuilder();

            if (Files.getFileStore(file).supportsFileAttributeView(UserDefinedFileAttributeView.class)
                    && Files.getFileAttributeView(file, UserDefinedFileAttributeView.class).list().contains(EoppHeaders.FILE_META.getHeader())) {
                FileMeta storedFileMeta = FileMeta.parseFrom((byte[]) Files.getAttribute(file, "user:" + EoppHeaders.FILE_META.getHeader()));
                builder.mergeFrom(storedFileMeta);
            }

            if (Strings.isNullOrEmpty(builder.getFilename())) {
                builder.setFilename(file.getFileName().toString());
            }

            if (builder.getSize() == 0 && !Files.isDirectory(file)) {
                builder.setSize(Files.size(file));
            }

            if (Strings.isNullOrEmpty(builder.getChecksum()) && !Files.isDirectory(file)) {
                builder.setChecksum(checksum(MoreFiles.asByteSource(file), checksumFunction));
            }

            if (!builder.hasLastModified()) {
                builder.setLastModified(Timestamps.timestampFromInstant(Files.getLastModifiedTime(file).toInstant()));
            }

            // These parameters should overwrite any existing FileMeta
            builder.setExecutable(executable);
            builder.putAllProperties(properties);

            return builder.build();
        } catch (IOException e) {
            throw new EoppFileException(e);
        }
    }

    /**
     * <p>Parse the given string as a base64-encoded representation of a FileMeta's bytes.</p>
     *
     * @return The FileMeta represented by the given base64-encoded string.
     * @see FileMeta#parseFrom(byte[])
     */
    public static FileMeta fromBase64(String string) {
        try {
            return FileMeta.parseFrom(Base64.getDecoder().decode(string));
        } catch (InvalidProtocolBufferException e) {
            throw new EoppFileException(e);
        }
    }

    /**
     * <p>Convert the given FileMeta into a base64-encoded representation of its raw bytes.</p>
     * <p>Suitable for when the Strings created by the built-in proto3 JsonFormat is not suitable, e.g. due to format
     * restrictions, or when proto2 features must be retained.</p>
     *
     * @return The base64-encoded byte representation of the given FileMeta.
     * @see FileMeta#toByteArray()
     */
    public static String toBase64(FileMeta fileMeta) {
        return Base64.getEncoder().encodeToString(fileMeta.toByteArray());
    }

    /**
     * @param byteSource The data to hash.
     * @return A checksum string of the given ByteSource, including the hashing algorithm as a prefix.
     */
    public static String checksum(ByteSource byteSource) throws IOException {
        return checksum(byteSource, DEFAULT_HASH_FUNCTION);
    }

    /**
     * @param byteSource       The data to hash.
     * @param checksumFunction The HashFunction with which to calculate the data checksum.
     * @return A checksum string of the given ByteSource, including the hashing algorithm as a prefix.
     */
    public static String checksum(ByteSource byteSource, HashFunction checksumFunction) throws IOException {
        return checksum(byteSource.hash(checksumFunction), checksumFunction);
    }

    /**
     * @param hashCode         A calculated hash of data.
     * @param checksumFunction The HashFunction with which the data checksum was calculated.
     * @return A checksum string of the given hash code, including the hashing algorithm as a prefix.
     */
    public static String checksum(HashCode hashCode, HashFunction checksumFunction) {
        return HASH_FUNCTIONS.get(checksumFunction) + ":" + hashCode.toString();
    }

    /**
     * @param fileMeta A FileMeta object referencing hashed data.
     * @return The {@link HashFunction} which was used to calculate the FileMeta's checksum. Returns an empty Optional
     * if no checksum is set on the FileMeta, or if the checksum is not in the recognised format with a known
     * HashFunction.
     */
    public static Optional<HashFunction> hashFunction(FileMeta fileMeta) {
        return Optional.ofNullable(HASH_FUNCTIONS.inverse().get(fileMeta.getChecksum().split(":", 2)[0]));
    }


}
