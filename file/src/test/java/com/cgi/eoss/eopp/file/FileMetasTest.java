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

import com.cgi.eoss.eopp.util.Timestamps;
import com.google.common.collect.ImmutableMap;
import com.google.common.hash.Hashing;
import com.google.protobuf.Any;
import com.google.protobuf.StringValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;
import static com.google.common.truth.extensions.proto.ProtoTruth.assertThat;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class FileMetasTest {

    @Test
    public void testGet() throws IOException {
        Path testfile = Files.createTempDirectory("testdata").resolve("testfile");
        Files.copy(getClass().getResourceAsStream("/testfile"), testfile, REPLACE_EXISTING);
        FileMeta fileMeta = FileMetas.get(testfile);
        assertThat(fileMeta).isEqualTo(FileMeta.newBuilder()
                .setFilename("testfile")
                .setSize(19L)
                .setChecksum("md5:a5890ace30a3e84d9118196c161aeec2")
                .setExecutable(false)
                .setLastModified(Timestamps.timestampFromInstant(Files.getLastModifiedTime(testfile).toInstant()))
                .build());
    }

    @Test
    public void testGetWithProperties() throws IOException {
        Path testfile = Files.createTempDirectory("testdata").resolve("testfile");
        Files.copy(getClass().getResourceAsStream("/testfile"), testfile, REPLACE_EXISTING);
        FileMeta fileMeta = FileMetas.get(testfile, ImmutableMap.of("foo", Any.pack(StringValue.of("bar"))));
        assertThat(fileMeta).isEqualTo(FileMeta.newBuilder()
                .setFilename("testfile")
                .setSize(19L)
                .setChecksum("md5:a5890ace30a3e84d9118196c161aeec2")
                .setExecutable(false)
                .putProperties("foo", Any.pack(StringValue.of("bar")))
                .setLastModified(Timestamps.timestampFromInstant(Files.getLastModifiedTime(testfile).toInstant()))
                .build());
    }

    @Test
    public void testGetWithPropertiesAndExecutable() throws IOException {
        Path testfile = Files.createTempDirectory("testdata").resolve("testfile");
        Files.copy(getClass().getResourceAsStream("/testfile"), testfile, REPLACE_EXISTING);
        FileMeta fileMeta = FileMetas.get(testfile, FileMetas.DEFAULT_HASH_FUNCTION, ImmutableMap.of("foo", Any.pack(StringValue.of("bar"))), true);
        assertThat(fileMeta).isEqualTo(FileMeta.newBuilder()
                .setFilename("testfile")
                .setSize(19L)
                .setChecksum("md5:a5890ace30a3e84d9118196c161aeec2")
                .setExecutable(true)
                .putProperties("foo", Any.pack(StringValue.of("bar")))
                .setLastModified(Timestamps.timestampFromInstant(Files.getLastModifiedTime(testfile).toInstant()))
                .build());
    }

    @Test
    public void testGetMurmur3_128() throws IOException {
        Path testfile = Files.createTempDirectory("testdata").resolve("testfile");
        Files.copy(getClass().getResourceAsStream("/testfile"), testfile, REPLACE_EXISTING);
        FileMeta fileMeta = FileMetas.get(testfile, Hashing.murmur3_128());
        assertThat(fileMeta).isEqualTo(FileMeta.newBuilder()
                .setFilename("testfile")
                .setSize(19L)
                .setChecksum("murmur3_128:82e72d37fbdc9b9109778d40f07aa303")
                .setExecutable(false)
                .setLastModified(Timestamps.timestampFromInstant(Files.getLastModifiedTime(testfile).toInstant()))
                .build());
    }

    @Test
    public void testGetSha256() throws IOException {
        Path testfile = Files.createTempDirectory("testdata").resolve("testfile");
        Files.copy(getClass().getResourceAsStream("/testfile"), testfile, REPLACE_EXISTING);
        FileMeta fileMeta = FileMetas.get(testfile, Hashing.sha256());
        assertThat(fileMeta).isEqualTo(FileMeta.newBuilder()
                .setFilename("testfile")
                .setSize(19L)
                .setChecksum("sha256:5881707e54b0112f901bc83a1ffbacac8fab74ea46a6f706a3efc5f7d4c1c625")
                .setExecutable(false)
                .setLastModified(Timestamps.timestampFromInstant(Files.getLastModifiedTime(testfile).toInstant()))
                .build());
    }

    @Test
    public void testGetSipHash24() throws IOException {
        Path testfile = Files.createTempDirectory("testdata").resolve("testfile");
        Files.copy(getClass().getResourceAsStream("/testfile"), testfile, REPLACE_EXISTING);
        FileMeta fileMeta = FileMetas.get(testfile, Hashing.sipHash24());
        assertThat(fileMeta).isEqualTo(FileMeta.newBuilder()
                .setFilename("testfile")
                .setSize(19L)
                .setChecksum("sipHash24:1b135005aeeb96f1")
                .setExecutable(false)
                .setLastModified(Timestamps.timestampFromInstant(Files.getLastModifiedTime(testfile).toInstant()))
                .build());
    }

    @Test
    public void testGetError() throws IOException {
        Path testfile = Paths.get("/nonexisting");

        try {
            FileMetas.get(testfile);
            fail("Expected exception");
        } catch (Throwable t) {
            assertThat(t).isInstanceOf(EoppFileException.class);
            assertThat(t).hasCauseThat().isInstanceOf(NoSuchFileException.class);
        }
    }

    @Test
    public void testToFromBase64() throws IOException {
        Path testfile = Files.createTempDirectory("testdata").resolve("testfile");
        Files.copy(getClass().getResourceAsStream("/testfile"), testfile, REPLACE_EXISTING);
        FileMeta fileMeta = FileMetas.get(testfile);
        String base64 = FileMetas.toBase64(fileMeta);

        FileMeta expected = FileMeta.newBuilder()
                .setFilename("testfile")
                .setSize(19L)
                .setChecksum("md5:a5890ace30a3e84d9118196c161aeec2")
                .setExecutable(false)
                .setLastModified(Timestamps.timestampFromInstant(Files.getLastModifiedTime(testfile).toInstant()))
                .build();
        assertThat(base64).isEqualTo(FileMetas.toBase64(expected));
        assertThat(FileMetas.fromBase64(base64)).isEqualTo(expected);
    }

    @Test
    public void testHashFunction() throws IOException {
        Path testfile = Files.createTempDirectory("testdata").resolve("testfile");
        Files.copy(getClass().getResourceAsStream("/testfile"), testfile, REPLACE_EXISTING);
        FileMeta fileMeta = FileMetas.get(testfile);

        assertThat(FileMetas.hashFunction(fileMeta)).hasValue(FileMetas.DEFAULT_HASH_FUNCTION);
    }

    @Test
    public void testHashFunctionSha256() throws IOException {
        Path testfile = Files.createTempDirectory("testdata").resolve("testfile");
        Files.copy(getClass().getResourceAsStream("/testfile"), testfile, REPLACE_EXISTING);
        FileMeta fileMeta = FileMetas.get(testfile, Hashing.sha256());

        assertThat(FileMetas.hashFunction(fileMeta)).hasValue(Hashing.sha256());
    }

    @Test
    public void testHashFunctionUnrecognisedHashFunction() throws IOException {
        Path testfile = Files.createTempDirectory("testdata").resolve("testfile");
        Files.copy(getClass().getResourceAsStream("/testfile"), testfile, REPLACE_EXISTING);
        FileMeta fileMeta = FileMetas.get(testfile, Hashing.goodFastHash(32));

        assertThat(FileMetas.hashFunction(fileMeta)).isEmpty();
    }

    @Test
    public void testHashFunctionEmpty() throws IOException {
        FileMeta fileMeta = FileMeta.getDefaultInstance();

        assertThat(FileMetas.hashFunction(fileMeta)).isEmpty();
    }

    @Test
    public void testHashFunctionUnrecognisedChecksumString() throws IOException {
        FileMeta fileMeta = FileMeta.newBuilder().setChecksum("some unrecognised checksum string").build();

        assertThat(FileMetas.hashFunction(fileMeta)).isEmpty();
    }

}