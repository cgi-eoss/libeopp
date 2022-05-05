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
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import com.google.protobuf.Any;
import com.google.protobuf.StringValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.extensions.proto.ProtoTruth.assertThat;

@RunWith(JUnit4.class)
public class EoppPathResourceTest {

    @Test
    public void testResourceProperties() throws IOException {
        Path testfile = Files.createTempFile("testfile", null);

        EoppResource resource = new EoppPathResource(testfile);
        assertThat(resource.getFile()).isEqualTo(testfile.toFile());
        assertThat(resource.getFileMeta()).isEqualTo(FileMetas.get(testfile));
        assertThat(resource.contentLength()).isEqualTo(Files.size(testfile));
        assertThat(resource.isCacheable()).isFalse();
        assertThat(resource.contentLength()).isEqualTo(Files.size(testfile));
        assertThat(resource.shouldRetry(new EoppResourceException("error"))).isFalse();
    }

    @Test
    public void testCustomFilename() throws IOException {
        Path testfile = Files.createTempFile("testfile", null);

        EoppResource resource = new EoppPathResource(testfile, "newfilename");
        assertThat(resource.getFileMeta())
                .isEqualTo(FileMetas.get(testfile).toBuilder().setFilename("newfilename").build());
    }

    @Test
    public void testReadExistingFileMeta() throws IOException {
        FileSystem fileSystem = Jimfs.newFileSystem(Configuration.unix().toBuilder().setAttributeViews("basic", "user").build());
        Path testfile = fileSystem.getPath("/testfile");
        Files.write(Files.createFile(testfile), "testfile".getBytes());

        FileMeta manualFileMeta = FileMeta.newBuilder()
                .setFilename("newfilename")
                .putProperties("custom-property", Any.pack(StringValue.of("custom-property-value")))
                .build();
        Files.setAttribute(testfile, "user:" + EoppHeaders.FILE_META.getHeader(), manualFileMeta.toByteArray());

        EoppResource resource = new EoppPathResource(testfile);
        assertThat(resource.getFileMeta()).comparingExpectedFieldsOnly().isEqualTo(manualFileMeta);
        assertThat(resource.getFileMeta().getChecksum()).isNotEmpty();
        assertThat(resource.getFileMeta().getLastModified()).isNotNull();
    }

}