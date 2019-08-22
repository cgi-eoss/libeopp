package com.cgi.eoss.eopp.file;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.io.MoreFiles;
import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;

/**
 * <p>Utility methods for working with {@link FileMeta} objects.</p>
 */
public final class FileMetas {

    /**
     * <p>The default HashFunction used for calculating FileMeta checksum attributes.</p>
     */
    public static final HashFunction DEFAULT_HASH_FUNCTION = Hashing.murmur3_128();

    /**
     * <p>Checksum HashFunctions with their well-known prefix strings.</p>
     */
    private static final BiMap<HashFunction, String> HASH_FUNCTIONS = ImmutableBiMap.<HashFunction, String>builder()
            .put(Hashing.sha256(), "sha256")
            .put(Hashing.murmur3_128(), "murmur3_128")
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
            return FileMeta.newBuilder()
                    .setFilename(file.getFileName().toString())
                    .setSize(Files.size(file))
                    .setChecksum(HASH_FUNCTIONS.get(checksumFunction) + ":" + MoreFiles.asByteSource(file).hash(checksumFunction).toString())
                    .setExecutable(executable)
                    .putAllProperties(properties)
                    .build();
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
    public static FileMeta fromBase64(String string) throws InvalidProtocolBufferException {
        return FileMeta.parseFrom(Base64.getDecoder().decode(string));
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

}
