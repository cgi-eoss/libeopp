package com.cgi.eoss.eopp.resource;

import com.cgi.eoss.eopp.file.FileMeta;
import com.cgi.eoss.eopp.file.FileMetas;
import com.cgi.eoss.eopp.util.EoppHeaders;
import com.cgi.eoss.eopp.util.Timestamps;
import com.google.common.io.MoreFiles;
import com.google.common.net.HttpHeaders;
import com.google.common.truth.extensions.proto.FieldScope;
import com.google.common.truth.extensions.proto.FieldScopes;
import com.google.protobuf.FieldMask;
import com.google.protobuf.Timestamp;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.checkerframework.checker.units.qual.A;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.extensions.proto.ProtoTruth.assertThat;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class EoppOkHttpResourceTest {

    private MockWebServer server;

    @Before
    public void setUp() throws IOException {
        server = new MockWebServer();

        server.start();
    }

    @After
    public void tearDown() throws IOException {
        server.shutdown();
    }

    @Test
    public void testResourceProperties() throws IOException {
        Path testfile = Files.createTempFile("testfile", null);
        Files.write(testfile, Arrays.asList("first", "second", "third"));
        HttpUrl url = server.url("/testfile");

        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + testfile.getFileName())
                .setHeader(HttpHeaders.LAST_MODIFIED, DateTimeFormatter.RFC_1123_DATE_TIME.format(Files.getLastModifiedTime(testfile).toInstant().atOffset(ZoneOffset.UTC)))
                .setHeader(HttpHeaders.CONTENT_LENGTH, Files.size(testfile))
                .setHeader(EoppHeaders.PRODUCT_ARCHIVE_CHECKSUM.getHeader(), FileMetas.checksum(MoreFiles.asByteSource(testfile)))
        );
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(new String(Files.readAllBytes(testfile)))
        );

        EoppResource resource = new EoppOkHttpResource(new OkHttpClient.Builder().build(), url);
        assertThat(resource.getFileMeta()).ignoringFieldScope( // HTTP response won't contain nanos, so match only seconds
                FieldScopes.ignoringFieldDescriptors(Timestamp.getDescriptor().findFieldByNumber(Timestamp.SECONDS_FIELD_NUMBER))
        ).isEqualTo(FileMetas.get(testfile));
        assertThat(resource.contentLength()).isEqualTo(Files.size(testfile));
        assertThat(resource.isCacheable()).isTrue();
        assertThat(resource.contentLength()).isEqualTo(Files.size(testfile));
        assertThat(resource.shouldRetry(null)).isFalse();
        assertThat(resource.shouldRetry(new SocketTimeoutException())).isTrue();
        assertThat(resource.exists()).isTrue();
        assertThat(resource.isReadable()).isTrue();
        assertThat(resource.getURI()).isEqualTo(url.uri());
        assertThat(resource.createRelative("otherfile").getURI()).isEqualTo(url.newBuilder().encodedPath("/otherfile").build().uri());

        try {
            resource.getFile();
            fail("Expected FileNotFoundException");
        }catch (FileNotFoundException e) {
            assertThat(e.getMessage()).isEqualTo("HTTP resources may not be resolved as Files");
        }
    }

}