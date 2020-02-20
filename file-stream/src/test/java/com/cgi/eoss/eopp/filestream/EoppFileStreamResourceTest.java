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

package com.cgi.eoss.eopp.filestream;

import com.cgi.eoss.eopp.file.FileChunk;
import com.cgi.eoss.eopp.file.FileMeta;
import com.cgi.eoss.eopp.file.FileMetas;
import com.cgi.eoss.eopp.rpc.GrpcMethod;
import com.google.common.io.ByteStreams;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.extensions.proto.ProtoTruth.assertThat;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class EoppFileStreamResourceTest {

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();
    private FileStreamTestServerGrpc.FileStreamTestServerStub fileServerStub;

    @Before
    public void setUp() throws Exception {
        String serverName = InProcessServerBuilder.generateName();

        grpcCleanup.register(InProcessServerBuilder.forName(serverName).directExecutor().addService(new FileStreamTestServer()).build().start());

        InProcessChannelBuilder channelBuilder = InProcessChannelBuilder.forName(serverName).directExecutor();
        fileServerStub = FileStreamTestServerGrpc.newStub(grpcCleanup.register(channelBuilder.build()));
    }

    @Test
    public void testResource() throws IOException {
        Path testfile = Paths.get("file-stream/src/test/resources/testfile");
        FileMeta fileMeta = FileMetas.get(testfile);

        GetFileParam getFile = GetFileParam.newBuilder().setUri(testfile.toUri().toString()).build();
        GrpcMethod<FileStreamTestServerGrpc.FileStreamTestServerStub, GetFileParam, FileChunk> getFileMethod
                = new GrpcMethod<>(fileServerStub, FileStreamTestServerGrpc.getGetFileMethod(), getFile);
        EoppFileStreamResource<FileStreamTestServerGrpc.FileStreamTestServerStub, GetFileParam> resource = new EoppFileStreamResource<>(fileMeta, getFileMethod);

        // Checksum the streamed data to verify correctness
        HashingCountingOutputStream target = new HashingCountingOutputStream(ByteStreams.nullOutputStream());
        try {
            ByteStreams.copy(resource.getInputStream(), target);
        } finally {
            target.close();
        }
        assertThat(target.getCount()).isEqualTo(fileMeta.getSize());
        assertThat(target.checksum()).isEqualTo(fileMeta.getChecksum());

        // Test all other resource properties
        assertThat(resource.exists()).isTrue();
        assertThat(resource.isCacheable()).isTrue();
        assertThat(resource.getFileMeta()).isEqualTo(fileMeta);
        assertThat(resource.getURI()).isEqualTo(URI.create("grpc://localhost/eopp.test.filestream.FileStreamTestServer/GetFile?uri=" + URLEncoder.encode(testfile.toUri().toString(), StandardCharsets.UTF_8.toString())));
        assertThat(resource.lastModified()).isEqualTo(Files.getLastModifiedTime(testfile).to(TimeUnit.SECONDS));
        assertThat(resource.getFilename()).isEqualTo("testfile");
        assertThat(resource.getDescription()).isEqualTo("Grpc File Stream [testfile]");

        // Unsupported operations
        try {
            resource.getFile();
            fail("Expected FileNotFoundException");
        } catch (FileNotFoundException expected) {
        }
        try {
            resource.getURL();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
        }
        try {
            resource.createRelative("foo");
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
        }
    }

}