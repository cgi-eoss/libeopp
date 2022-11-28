/*
 * Copyright 2022 The libeopp Team
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
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;
import com.google.common.io.ByteStreams;
import com.google.common.truth.extensions.proto.ProtoTruth;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
@Ignore("Requires local GCP client environment configuration")
public class EoppGoogleCloudStorageObjectResourceTest {

    private static final String TEST_BUCKET = "libeopp-test";

    private static Storage storage;

    @BeforeClass
    public static void setUp() throws IOException {
        storage = StorageOptions.newBuilder().build().getService();

        try {
            storage.create(BucketInfo.of(TEST_BUCKET));
        } catch (StorageException e) {
            if (e.getCode() != 409) {
                throw e;
            }
        }
    }

    @AfterClass
    public static void tearDown() throws IOException {
        Page<Blob> blobs = storage.list(TEST_BUCKET);
        for (Blob blob : blobs.iterateAll()) {
            storage.delete(blob.getBlobId());
        }
        storage.delete(TEST_BUCKET);
    }

    @Test
    public void testResourceProperties() throws IOException {
        Path testfile = Files.createTempFile("testfile", null);
        Files.write(testfile, Arrays.asList("first", "second", "third"));

        String blobName = UUID.randomUUID().toString();

        Blob blob = storage.createFrom(BlobInfo.newBuilder(TEST_BUCKET, blobName)
                .setMetadata(Map.of(
                        EoppHeaders.FILE_META.getHeader(), FileMetas.toBase64(FileMetas.get(testfile))
                ))
                .build(), testfile);

        EoppGoogleCloudStorageObjectResource resource = new EoppGoogleCloudStorageObjectResource(storage, blob.getBucket(), blob.getName());
        ProtoTruth.assertThat(resource.getFileMeta()).isEqualTo(FileMetas.get(testfile));
        assertThat(resource.contentLength()).isEqualTo(Files.size(testfile));
        assertThat(resource.isCacheable()).isTrue();
        assertThat(resource.shouldRetry(new EoppResourceException("error"))).isFalse();
        assertThat(resource.exists()).isTrue();
        assertThat(resource.isReadable()).isTrue();
        assertThat(resource.getURI()).isEqualTo(URI.create("gcs://" + TEST_BUCKET + "/" + blobName));
        assertThat(resource.createRelative("otherfile").getURI()).isEqualTo(URI.create("gcs://" + TEST_BUCKET + "/otherfile"));

        try {
            resource.getFile();
            fail("Expected FileNotFoundException");
        } catch (FileNotFoundException e) {
            assertThat(e.getMessage()).isEqualTo("Google Cloud Storage resources may not be resolved as Files");
        }

        HashingCountingOutputStream os = new HashingCountingOutputStream(ByteStreams.nullOutputStream());
        try (InputStream is = resource.getInputStream(); os) {
            ByteStreams.copy(is, os);
        }
        assertThat(os.getCount()).isEqualTo(FileMetas.get(testfile).getSize());
        assertThat(os.checksum()).isEqualTo(FileMetas.get(testfile).getChecksum());
    }

    @Test
    public void testResourceNotFound() throws IOException {
        String blobName = UUID.randomUUID().toString();

        EoppGoogleCloudStorageObjectResource resource = new EoppGoogleCloudStorageObjectResource(storage, TEST_BUCKET, blobName);
        ProtoTruth.assertThat(resource.getFileMeta()).isEqualTo(FileMeta.getDefaultInstance());
        assertThat(resource.contentLength()).isEqualTo(-1);
        assertThat(resource.isCacheable()).isTrue();
        assertThat(resource.shouldRetry(new EoppResourceException("error"))).isFalse();
        assertThat(resource.exists()).isFalse();
        assertThat(resource.isReadable()).isFalse();
        assertThat(resource.getURI()).isEqualTo(URI.create("gcs://" + TEST_BUCKET + "/" + blobName));
        assertThat(resource.createRelative("otherfile").getURI()).isEqualTo(URI.create("gcs://" + TEST_BUCKET + "/otherfile"));

        try {
            resource.getFile();
            fail("Expected FileNotFoundException");
        } catch (FileNotFoundException e) {
            assertThat(e.getMessage()).isEqualTo("Google Cloud Storage resources may not be resolved as Files");
        }
    }

}
