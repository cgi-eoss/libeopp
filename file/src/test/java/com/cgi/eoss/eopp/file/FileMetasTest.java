package com.cgi.eoss.eopp.file;

import com.google.common.collect.ImmutableMap;
import com.google.common.hash.Hashing;
import com.google.protobuf.Any;
import com.google.protobuf.StringValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.extensions.proto.ProtoTruth.assertThat;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class FileMetasTest {

    @Test
    public void testGet() throws IOException {
        Path testfile = Paths.get("file/src/test/resources/testfile");
        FileMeta fileMeta = FileMetas.get(testfile);
        assertThat(fileMeta).isEqualTo(FileMeta.newBuilder()
                .setFilename("testfile")
                .setSize(19L)
                .setChecksum("murmur3_128:82e72d37fbdc9b9109778d40f07aa303")
                .setExecutable(false)
                .build());
    }

    @Test
    public void testGetWithProperties() throws IOException {
        Path testfile = Paths.get("file/src/test/resources/testfile");
        FileMeta fileMeta = FileMetas.get(testfile, ImmutableMap.of("foo", Any.pack(StringValue.of("bar"))));
        assertThat(fileMeta).isEqualTo(FileMeta.newBuilder()
                .setFilename("testfile")
                .setSize(19L)
                .setChecksum("murmur3_128:82e72d37fbdc9b9109778d40f07aa303")
                .setExecutable(false)
                .putProperties("foo", Any.pack(StringValue.of("bar")))
                .build());
    }

    @Test
    public void testGetWithPropertiesAndExecutable() throws IOException {
        Path testfile = Paths.get("file/src/test/resources/testfile");
        FileMeta fileMeta = FileMetas.get(testfile, FileMetas.DEFAULT_HASH_FUNCTION, ImmutableMap.of("foo", Any.pack(StringValue.of("bar"))), true);
        assertThat(fileMeta).isEqualTo(FileMeta.newBuilder()
                .setFilename("testfile")
                .setSize(19L)
                .setChecksum("murmur3_128:82e72d37fbdc9b9109778d40f07aa303")
                .setExecutable(true)
                .putProperties("foo", Any.pack(StringValue.of("bar")))
                .build());
    }

    @Test
    public void testGetSha256() throws IOException {
        Path testfile = Paths.get("file/src/test/resources/testfile");
        FileMeta fileMeta = FileMetas.get(testfile, Hashing.sha256());
        assertThat(fileMeta).isEqualTo(FileMeta.newBuilder()
                .setFilename("testfile")
                .setSize(19L)
                .setChecksum("sha256:5881707e54b0112f901bc83a1ffbacac8fab74ea46a6f706a3efc5f7d4c1c625")
                .setExecutable(false)
                .build());
    }

    @Test
    public void testGetError() throws IOException {
        Path testfile = Paths.get("file/src/test/resources/nonexisting");

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
        Path testfile = Paths.get("file/src/test/resources/testfile");
        FileMeta fileMeta = FileMetas.get(testfile);
        String base64 = FileMetas.toBase64(fileMeta);

        FileMeta expected = FileMeta.newBuilder()
                .setFilename("testfile")
                .setSize(19L)
                .setChecksum("murmur3_128:82e72d37fbdc9b9109778d40f07aa303")
                .setExecutable(false)
                .build();
        assertThat(base64).isEqualTo(FileMetas.toBase64(expected));
        assertThat(FileMetas.fromBase64(base64)).isEqualTo(expected);
    }

}