package com.cgi.eoss.eopp.resource;

import com.cgi.eoss.eopp.file.FileMeta;
import com.cgi.eoss.eopp.file.FileMetas;
import com.cgi.eoss.eopp.util.EoppHeaders;
import com.google.common.io.MoreFiles;
import com.google.common.net.HttpHeaders;
import com.google.common.truth.extensions.proto.FieldScopes;
import com.google.common.truth.extensions.proto.ProtoTruth;
import com.google.protobuf.Timestamp;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class EoppS3ObjectResourceTest {
    private static final Logger log = LoggerFactory.getLogger(EoppS3ObjectResourceTest.class);

    private MockWebServer server;
    private S3AsyncClient asyncS3Client;

    @Before
    public void setUp() throws IOException {
        server = new MockWebServer();

        server.start();

        asyncS3Client = S3AsyncClient.builder()
                .endpointOverride(server.url("/").uri())
                .region(Region.of("LOCAL"))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("default", "default")))
                .build();
    }

    @After
    public void tearDown() throws IOException {
        server.shutdown();
    }

    @Test
    public void testResourceProperties() throws IOException {
        Path testfile = Files.createTempFile("testfile", null);
        Files.write(testfile, Arrays.asList("first", "second", "third"));

        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + testfile.getFileName())
                .setHeader(HttpHeaders.LAST_MODIFIED, DateTimeFormatter.RFC_1123_DATE_TIME.format(Files.getLastModifiedTime(testfile).toInstant().atOffset(ZoneOffset.UTC)))
                .setHeader(HttpHeaders.CONTENT_LENGTH, Files.size(testfile))
                .setHeader("x-amz-meta-" + EoppHeaders.PRODUCT_ARCHIVE_CHECKSUM.getHeader(), FileMetas.checksum(MoreFiles.asByteSource(testfile)))
        );
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(new String(Files.readAllBytes(testfile)))
        );

        EoppResource resource = new EoppS3ObjectResource(asyncS3Client, "EODATA", "testfile");
        ProtoTruth.assertThat(resource.getFileMeta()) // S3 HTTP response won't contain nanos, so match only seconds
                .withPartialScope(FieldScopes.ignoringFields(FileMeta.LAST_MODIFIED_FIELD_NUMBER))
                .isEqualTo(FileMetas.get(testfile));
        ProtoTruth.assertThat(resource.getFileMeta().getLastModified())
                .withPartialScope(FieldScopes.ignoringFields(Timestamp.NANOS_FIELD_NUMBER))
                .isEqualTo(FileMetas.get(testfile).getLastModified());
        assertThat(resource.contentLength()).isEqualTo(Files.size(testfile));
        assertThat(resource.isCacheable()).isTrue();
        assertThat(resource.contentLength()).isEqualTo(Files.size(testfile));
        assertThat(resource.shouldRetry(null)).isFalse();
        assertThat(resource.exists()).isTrue();
        assertThat(resource.isReadable()).isTrue();
        assertThat(resource.getURI()).isEqualTo(URI.create("s3://EODATA/testfile"));
        assertThat(resource.createRelative("otherfile").getURI()).isEqualTo(URI.create("s3://EODATA/otherfile"));

        try {
            resource.getFile();
            fail("Expected FileNotFoundException");
        } catch (FileNotFoundException e) {
            assertThat(e.getMessage()).isEqualTo("S3 resources may not be resolved as Files");
        }
//
//        EoppS3ObjectResource resource = new EoppS3ObjectResource(asyncS3Client, "EODATA", "Sentinel-1/SAR/SLC/2018/11/06/S1A_IW_SLC__1SDV_20181106T081501_20181106T081527_024464_02AE99_0F73.SAFE/manifest.safe");
//
//        assertThat(resource.exists()).isTrue();
//
//        InputStream in = resource.getInputStream();
//        HashingCountingOutputStream out = new HashingCountingOutputStream(ByteStreams.nullOutputStream());
//        try {
//            ByteStreams.copy(in, out);
//        } finally {
//            in.close();
//            out.close();
//        }
//        assertThat(out.getCount()).isEqualTo(36536);
    }

}