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

package com.cgi.eoss.eopp.resource;

import com.cgi.eoss.eopp.file.FileMeta;
import com.cgi.eoss.eopp.file.FileMetas;
import com.cgi.eoss.eopp.util.EoppHeaders;
import com.cgi.eoss.eopp.util.HashingCountingOutputStream;
import com.google.common.io.ByteStreams;
import com.google.common.io.MoreFiles;
import com.google.common.net.HttpHeaders;
import com.google.common.truth.extensions.proto.FieldScopes;
import com.google.common.truth.extensions.proto.ProtoTruth;
import com.google.protobuf.Timestamp;
import mockwebserver3.Dispatcher;
import mockwebserver3.MockResponse;
import mockwebserver3.RecordedRequest;
import mockwebserver3.junit4.MockWebServerRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class EoppS3ObjectResourceTest {
    private S3Client s3Client;

    @Rule
    public final MockWebServerRule serverRule = new MockWebServerRule();

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException {
        s3Client = S3Client.builder()
                .endpointOverride(serverRule.getServer().url("/").uri())
                .region(Region.EU_WEST_1)
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("default", "default")))
                .build();
    }

    @Test
    public void testResourceProperties() throws IOException {
        Path testfile = Files.createTempFile("testfile", null);
        Files.write(testfile, Arrays.asList("first", "second", "third"));

        serverRule.getServer().enqueue(new MockResponse.Builder()
                .code(200)
                .setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + testfile.getFileName())
                .setHeader(HttpHeaders.LAST_MODIFIED, DateTimeFormatter.RFC_1123_DATE_TIME.format(Files.getLastModifiedTime(testfile).toInstant().atOffset(ZoneOffset.UTC)))
                .setHeader(HttpHeaders.CONTENT_LENGTH, Files.size(testfile))
                .setHeader("x-amz-meta-" + EoppHeaders.PRODUCT_ARCHIVE_CHECKSUM.getHeader(), FileMetas.checksum(MoreFiles.asByteSource(testfile)))
                .build()
        );

        EoppResource resource = new EoppS3ObjectResource(s3Client, "EODATA", "testfile");
        ProtoTruth.assertThat(resource.getFileMeta()) // S3 HTTP response won't contain nanos, so match only seconds
                .withPartialScope(FieldScopes.ignoringFields(FileMeta.LAST_MODIFIED_FIELD_NUMBER))
                .isEqualTo(FileMetas.get(testfile));
        ProtoTruth.assertThat(resource.getFileMeta().getLastModified())
                .withPartialScope(FieldScopes.ignoringFields(Timestamp.NANOS_FIELD_NUMBER))
                .isEqualTo(FileMetas.get(testfile).getLastModified());
        assertThat(resource.contentLength()).isEqualTo(Files.size(testfile));
        assertThat(resource.isCacheable()).isTrue();
        assertThat(resource.shouldRetry(new EoppResourceException("error"))).isFalse();
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

        serverRule.getServer().enqueue(new MockResponse.Builder()
                .code(200)
                .body(new String(Files.readAllBytes(testfile)))
                .build()
        );

        HashingCountingOutputStream os = new HashingCountingOutputStream(ByteStreams.nullOutputStream());
        try (InputStream is = resource.getInputStream(); os) {
            ByteStreams.copy(is, os);
        }
        assertThat(os.getCount()).isEqualTo(FileMetas.get(testfile).getSize());
        assertThat(os.checksum()).isEqualTo(FileMetas.get(testfile).getChecksum());
    }

    @Test
    public void testResourceNotFound() throws IOException {
        Path testfile = Files.createTempFile("testfile", null);
        Files.write(testfile, Arrays.asList("first", "second", "third"));

        serverRule.getServer().enqueue(new MockResponse.Builder().code(404).build());

        EoppResource resource = new EoppS3ObjectResource(s3Client, "EODATA", "testfile");
        ProtoTruth.assertThat(resource.getFileMeta()).isEqualTo(FileMeta.getDefaultInstance());
        assertThat(resource.contentLength()).isEqualTo(-1);
        assertThat(resource.isCacheable()).isTrue();
        assertThat(resource.shouldRetry(new EoppResourceException("error"))).isFalse();
        assertThat(resource.exists()).isFalse();
        assertThat(resource.isReadable()).isFalse();
        assertThat(resource.getURI()).isEqualTo(URI.create("s3://EODATA/testfile"));
        assertThat(resource.createRelative("otherfile").getURI()).isEqualTo(URI.create("s3://EODATA/otherfile"));

        try {
            resource.getFile();
            fail("Expected FileNotFoundException");
        } catch (FileNotFoundException e) {
            assertThat(e.getMessage()).isEqualTo("S3 resources may not be resolved as Files");
        }
    }

    @Test
    public void testRequesterPays() throws Exception {
        Path testfile = Files.createTempFile("testfile", null);
        Files.write(testfile, Arrays.asList("first", "second", "third"));

        serverRule.getServer().setDispatcher(new RequesterPaysDispatcher());

        serverRule.getServer().enqueue(new MockResponse.Builder()
                .code(200)
                .setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + testfile.getFileName())
                .setHeader(HttpHeaders.LAST_MODIFIED, DateTimeFormatter.RFC_1123_DATE_TIME.format(Files.getLastModifiedTime(testfile).toInstant().atOffset(ZoneOffset.UTC)))
                .setHeader(HttpHeaders.CONTENT_LENGTH, Files.size(testfile))
                .setHeader("x-amz-meta-" + EoppHeaders.PRODUCT_ARCHIVE_CHECKSUM.getHeader(), FileMetas.checksum(MoreFiles.asByteSource(testfile)))
                .build()
        );
        serverRule.getServer().enqueue(new MockResponse.Builder()
                .code(200)
                .body(new String(Files.readAllBytes(testfile)))
                .build()
        );

        EoppResource resource = new EoppS3ObjectResource(s3Client, "EODATA", "testfile", true);
        ProtoTruth.assertThat(resource.getFileMeta()) // S3 HTTP response won't contain nanos, so match only seconds
                .withPartialScope(FieldScopes.ignoringFields(FileMeta.LAST_MODIFIED_FIELD_NUMBER))
                .isEqualTo(FileMetas.get(testfile));
        ProtoTruth.assertThat(resource.getFileMeta().getLastModified())
                .withPartialScope(FieldScopes.ignoringFields(Timestamp.NANOS_FIELD_NUMBER))
                .isEqualTo(FileMetas.get(testfile).getLastModified());
        assertThat(resource.contentLength()).isEqualTo(Files.size(testfile));
        assertThat(resource.isCacheable()).isTrue();
        assertThat(resource.shouldRetry(new EoppResourceException("error"))).isFalse();
        assertThat(resource.exists()).isTrue();
        assertThat(resource.isReadable()).isTrue();
        assertThat(resource.getURI()).isEqualTo(URI.create("s3://EODATA/testfile"));
        assertThat(resource.createRelative("otherfile").getURI()).isEqualTo(URI.create("s3://EODATA/otherfile"));
    }

    @Test
    public void testRequesterPaysError() throws Exception {
        Path testfile = Files.createTempFile("testfile", null);
        Files.write(testfile, Arrays.asList("first", "second", "third"));

        serverRule.getServer().setDispatcher(new RequesterPaysDispatcher());

        serverRule.getServer().enqueue(new MockResponse.Builder()
                .code(200)
                .setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + testfile.getFileName())
                .setHeader(HttpHeaders.LAST_MODIFIED, DateTimeFormatter.RFC_1123_DATE_TIME.format(Files.getLastModifiedTime(testfile).toInstant().atOffset(ZoneOffset.UTC)))
                .setHeader(HttpHeaders.CONTENT_LENGTH, Files.size(testfile))
                .setHeader("x-amz-meta-" + EoppHeaders.PRODUCT_ARCHIVE_CHECKSUM.getHeader(), FileMetas.checksum(MoreFiles.asByteSource(testfile)))
                .build()
        );
        serverRule.getServer().enqueue(new MockResponse.Builder()
                .code(200)
                .body(new String(Files.readAllBytes(testfile)))
                .build()
        );

        EoppResource resource = new EoppS3ObjectResource(s3Client, "EODATA", "testfile");
        try {
            resource.getFileMeta();
        } catch (Exception expected) {
            assertThat(expected).hasCauseThat().isInstanceOf(S3Exception.class);
            assertThat(expected).hasCauseThat().hasMessageThat().contains("Status Code: 403");
        }
    }

    @Test
    public void testZipS3Resources() throws IOException {
        serverRule.getServer().setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest recordedRequest) throws InterruptedException {
                String filename = StringUtils.getFilename(recordedRequest.getUrl().encodedPath());
                final MockResponse.Builder mockResponse = new MockResponse.Builder()
                        .code(200)
                        .setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                        .setHeader(HttpHeaders.LAST_MODIFIED, DateTimeFormatter.RFC_1123_DATE_TIME.format(Instant.now().atOffset(ZoneOffset.UTC)))
                        .setHeader(HttpHeaders.CONTENT_LENGTH, "1024");
                return switch (Objects.requireNonNull(recordedRequest.getMethod())) {
                    case "GET" -> mockResponse.body(new String(new byte[1024])).build();
                    case "HEAD" -> mockResponse.build();
                    default -> throw new IllegalStateException("Unexpected value: " + recordedRequest.getMethod());
                };
            }
        });

        EoppResource resource1 = new EoppS3ObjectResource(s3Client, "EODATA", "testfile1");
        EoppResource resource2 = new EoppS3ObjectResource(s3Client, "EODATA", "testfile2");

        EoppZipCombiningResource zipResource = new EoppZipCombiningResource(List.of(
                new ZipResourceEntry("subdir1/", FileTime.fromMillis(resource1.lastModified())),
                new ZipResourceEntry("subdir1/testfile", resource1),
                new ZipResourceEntry("subdir2/", FileTime.fromMillis(resource2.lastModified())),
                new ZipResourceEntry("subdir2/testfile2", resource2)
        ), false);
        assertThat(zipResource.getDescription()).isEqualTo("ZipFile (compression: -1) [ " + resource1.getDescription() + "," + resource2.getDescription() + " ]");
        assertThat(zipResource.getFileMeta().getChecksum()).isEmpty();
        assertThat(zipResource.getFileMeta().getFilename()).isEmpty();
        assertThat(zipResource.getOriginalSize()).isEqualTo(-1);

        // Save the zip file and check its contents

        Path zipFile = temporaryFolder.newFile().toPath();
        try (InputStream inputStream = new BufferedInputStream(zipResource.getInputStream())) {
            Files.copy(inputStream, zipFile, StandardCopyOption.REPLACE_EXISTING);
        }

        try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(zipFile))) {
            ZipEntry zipEntry = zis.getNextEntry();
            assertThat(zipEntry.getName()).isEqualTo("subdir1/");
            assertThat(zipEntry.isDirectory()).isTrue();

            zipEntry = zis.getNextEntry();
            assertThat(zipEntry.getName()).isEqualTo("subdir1/testfile");
            assertThat(zipEntry.isDirectory()).isFalse();
            byte[] testfile1 = zis.readAllBytes();
            assertThat(testfile1).hasLength(1024);
            assertThat(new String(testfile1)).isEqualTo(new String(new byte[1024]));

            zipEntry = zis.getNextEntry();
            assertThat(zipEntry.getName()).isEqualTo("subdir2/");
            assertThat(zipEntry.isDirectory()).isTrue();

            zipEntry = zis.getNextEntry();
            assertThat(zipEntry.getName()).isEqualTo("subdir2/testfile2");
            assertThat(zipEntry.isDirectory()).isFalse();
            byte[] testfile2 = zis.readAllBytes();
            assertThat(testfile2).hasLength(1024);
            assertThat(new String(testfile2)).isEqualTo(new String(new byte[1024]));
        }
    }

}