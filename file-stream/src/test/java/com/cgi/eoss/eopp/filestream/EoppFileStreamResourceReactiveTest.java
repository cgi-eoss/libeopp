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
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.google.common.truth.Truth.assertThat;

@RunWith(JUnit4.class)
public class EoppFileStreamResourceReactiveTest {

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();
    private ReactorFileStreamTestServerGrpc.ReactorFileStreamTestServerStub reactiveFileServerStub;

    @Before
    public void setUp() throws Exception {
        String serverName = InProcessServerBuilder.generateName();

        grpcCleanup.register(InProcessServerBuilder.forName(serverName).directExecutor().addService(new ReactiveFileStreamTestServer()).build().start());

        InProcessChannelBuilder channelBuilder = InProcessChannelBuilder.forName(serverName).directExecutor();
        reactiveFileServerStub = ReactorFileStreamTestServerGrpc.newReactorStub(grpcCleanup.register(channelBuilder.build()));
    }

    @Test
    public void testReactiveResource() throws IOException {
        Path testfile = Paths.get("file-stream/src/test/resources/testfile");
        FileMeta fileMeta = FileMetas.get(testfile);

        GetFileParam getFile = GetFileParam.newBuilder().setUri(testfile.toUri().toString()).build();
        GrpcMethod<ReactorFileStreamTestServerGrpc.ReactorFileStreamTestServerStub, GetFileParam, FileChunk> getFileMethod
                = new GrpcMethod<>(reactiveFileServerStub, FileStreamTestServerGrpc.getGetFileMethod(), getFile);
        EoppFileStreamResource<ReactorFileStreamTestServerGrpc.ReactorFileStreamTestServerStub, GetFileParam> resource = new EoppFileStreamResource<>(fileMeta, getFileMethod, reactiveFileServerStub::getFile);

        HashingCountingOutputStream target = new HashingCountingOutputStream(ByteStreams.nullOutputStream());
        try {
            ByteStreams.copy(resource.getInputStream(), target);
        } finally {
            target.close();
        }
        assertThat(target.getCount()).isEqualTo(fileMeta.getSize());
        assertThat(target.checksum()).isEqualTo(fileMeta.getChecksum());
    }

    @Test
    public void testReactiveDataStream() throws IOException {
        Path testfile = Paths.get("file-stream/src/test/resources/testfile");
        FileMeta fileMeta = FileMetas.get(testfile);

        GetFileParam getFile = GetFileParam.newBuilder().setUri(testfile.toUri().toString()).build();

        HashingCountingOutputStream target = new HashingCountingOutputStream(ByteStreams.nullOutputStream());
        try {
            FileStreams.writeToStream(Mono.just(getFile), reactiveFileServerStub::getFile, target);
        } finally {
            target.close();
        }
        assertThat(target.getCount()).isEqualTo(fileMeta.getSize());
        assertThat(target.checksum()).isEqualTo(fileMeta.getChecksum());
    }

}