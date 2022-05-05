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

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;
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
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class EoppAzureBlobResourceTest {
    private MockWebServer server;
    private BlobServiceClient blobServiceClient;

    @Before
    public void setUp() throws IOException {
        server = new MockWebServer();

        server.start();

        blobServiceClient = new BlobServiceClientBuilder()
                .endpoint(server.url("/").toString())
                .credential(new StorageSharedKeyCredential("default", "default"))
                .buildClient();
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
                .setHeader("x-ms-meta-" + EoppHeaders.PRODUCT_ARCHIVE_CHECKSUM.getHeader().replace('-', '_'), FileMetas.checksum(MoreFiles.asByteSource(testfile)))
        );

        EoppResource resource = new EoppAzureBlobResource(blobServiceClient, "EODATA", "testfile");
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
        assertThat(resource.getURI()).isEqualTo(server.url("/EODATA/testfile").uri());
        assertThat(resource.createRelative("otherfile").getURI()).isEqualTo(server.url("/EODATA/otherfile").uri());

        try {
            resource.getFile();
            fail("Expected FileNotFoundException");
        } catch (FileNotFoundException e) {
            assertThat(e.getMessage()).isEqualTo("Azure Blob resources may not be resolved as Files");
        }

        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + testfile.getFileName())
                .setHeader(HttpHeaders.LAST_MODIFIED, DateTimeFormatter.RFC_1123_DATE_TIME.format(Files.getLastModifiedTime(testfile).toInstant().atOffset(ZoneOffset.UTC)))
                .setHeader(HttpHeaders.CONTENT_LENGTH, new String(Files.readAllBytes(testfile)).getBytes().length)
                .setHeader(HttpHeaders.CONTENT_RANGE, String.format("0-%d/%d", Files.size(testfile)-1, Files.size(testfile)))
                .setHeader("x-ms-meta-" + EoppHeaders.PRODUCT_ARCHIVE_CHECKSUM.getHeader().replace('-', '_'), FileMetas.checksum(MoreFiles.asByteSource(testfile)))
                .setBody(new String(Files.readAllBytes(testfile)))
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

        server.enqueue(new MockResponse().setResponseCode(404)
                .setHeader("x-ms-error-code", "BlobNotFound"));

        EoppResource resource = new EoppAzureBlobResource(blobServiceClient, "EODATA", "testfile");
        ProtoTruth.assertThat(resource.getFileMeta()).isEqualTo(FileMeta.getDefaultInstance());
        assertThat(resource.contentLength()).isEqualTo(-1);
        assertThat(resource.isCacheable()).isTrue();
        assertThat(resource.shouldRetry(new EoppResourceException("error"))).isFalse();
        assertThat(resource.exists()).isFalse();
        assertThat(resource.isReadable()).isFalse();
        assertThat(resource.getURI()).isEqualTo(server.url("/EODATA/testfile").uri());
        assertThat(resource.createRelative("otherfile").getURI()).isEqualTo(server.url("/EODATA/otherfile").uri());

        try {
            resource.getFile();
            fail("Expected FileNotFoundException");
        } catch (FileNotFoundException e) {
            assertThat(e.getMessage()).isEqualTo("Azure Blob resources may not be resolved as Files");
        }
    }

}