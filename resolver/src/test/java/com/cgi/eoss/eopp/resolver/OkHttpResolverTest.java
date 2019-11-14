package com.cgi.eoss.eopp.resolver;

import com.cgi.eoss.eopp.file.FileMeta;
import com.cgi.eoss.eopp.file.FileMetas;
import com.cgi.eoss.eopp.resource.EoppResource;
import com.cgi.eoss.eopp.util.EoppHeaders;
import com.cgi.eoss.eopp.util.Timestamps;
import com.google.common.io.ByteStreams;
import com.google.common.io.MoreFiles;
import com.google.common.net.HttpHeaders;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.extensions.proto.ProtoTruth.assertThat;

@RunWith(JUnit4.class)
public class OkHttpResolverTest {

    private OkHttpResolver resolver;
    private MockWebServer server;

    @Before
    public void setUp() throws IOException {
        resolver = new OkHttpResolver();
        server = new MockWebServer();

        server.start();
    }

    @After
    public void tearDown() throws IOException {
        server.shutdown();
    }

    @Test
    public void canResolveTest() {
        URI fileUri = URI.create("file:///path/to/file");
        URI httpUri = URI.create("http://example.com/file");
        URI httpsUri = URI.create("https://example.com/file");

        assertThat(resolver.canResolve(fileUri)).isFalse();
        assertThat(resolver.canResolve(httpUri)).isTrue();
        assertThat(resolver.canResolve(httpsUri)).isTrue();
    }

    @Test
    public void resolveUri() throws IOException, InterruptedException {
        Path testfile = Files.createTempFile("testfile", null);
        Files.write(testfile, Arrays.asList("first", "second", "third"));
        HttpUrl url = server.url("/testfile");

        ZonedDateTime zonedDateTime = ZonedDateTime.now().withNano(0);

        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=testfile")
                .setHeader(HttpHeaders.LAST_MODIFIED, DateTimeFormatter.RFC_1123_DATE_TIME.format(zonedDateTime))
                .setHeader(HttpHeaders.CONTENT_LENGTH, Files.size(testfile))
                .setHeader(EoppHeaders.PRODUCT_ARCHIVE_CHECKSUM.getHeader(), FileMetas.checksum(MoreFiles.asByteSource(testfile)))
        );
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(new String(Files.readAllBytes(testfile)))
        );

        FileMeta expectedFileMeta = FileMeta.newBuilder()
                .setFilename("testfile")
                .setSize(Files.size(testfile))
                .setLastModified(Timestamps.timestampFromInstant(zonedDateTime.toInstant()))
                .setExecutable(false)
                .setChecksum(FileMetas.checksum(MoreFiles.asByteSource(testfile)))
                .build();

        EoppResource resource = resolver.resolveUri(url.uri());
        assertThat(resource).isNotNull();
        assertThat(resource.getFileMeta()).isEqualTo(expectedFileMeta);

        Path receivedFile = Files.createTempFile("received", null);
        ByteStreams.copy(resource.getInputStream(), Files.newOutputStream(receivedFile));
        assertThat(FileMetas.get(receivedFile))
                .ignoringFields(FileMeta.FILENAME_FIELD_NUMBER, FileMeta.LAST_MODIFIED_FIELD_NUMBER)
                .isEqualTo(expectedFileMeta);

        // First a HEAD request for the metadata
        RecordedRequest headRequest = server.takeRequest();
        assertThat(headRequest.getMethod()).isEqualTo("HEAD");

        // Second the GET request for the data
        RecordedRequest getRequest = server.takeRequest();
        assertThat(getRequest.getMethod()).isEqualTo("GET");
    }

}