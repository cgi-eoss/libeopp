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
import com.cgi.eoss.eopp.resource.EoppResource;
import com.cgi.eoss.eopp.rpc.GrpcMethod;
import com.google.common.base.Preconditions;
import com.google.protobuf.Message;
import io.grpc.stub.AbstractStub;
import io.grpc.stub.ClientCalls;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.URI;
import java.net.URL;
import java.util.function.Function;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * <p>An {@link EoppResource} backed by a gRPC stream of {@link FileChunk} messages.</p>
 *
 * @param <S> The gRPC service stub type.
 * @param <P> The gRPC service parameter (request) message type.
 */
public class EoppFileStreamResource<S extends AbstractStub<S>, P extends Message> implements EoppResource {

    private static final Logger log = getLogger(EoppFileStreamResource.class);

    private final FileMeta fileMeta;
    private final GrpcMethod<S, P, FileChunk> fileStreamMethod;
    private final Function<Mono<P>, Flux<FileChunk>> reactiveMethodRef;

    /**
     * <p>Create a new EoppFileStreamResource. The {@link #getInputStream()} method will resolve the data with the given
     * gRPC method reference.</p>
     *
     * @param fileMeta         FileMeta for the resulting resource.
     * @param fileStreamMethod gRPC method returning <code>stream FileChunk</code>; the data backing this resource.
     * @see #EoppFileStreamResource(FileMeta, GrpcMethod, Function) Use the reactive API constructor to make use of
     * built-in flow control and other enhancements.
     */
    public EoppFileStreamResource(FileMeta fileMeta, GrpcMethod<S, P, FileChunk> fileStreamMethod) {
        this(fileMeta, fileStreamMethod, null);
    }

    /**
     * <p>Create a new EoppFileStreamResource. The {@link #getInputStream()} method will resolve the data with the given
     * Reactive-gRPC method reference.</p>
     *
     * @param fileMeta          FileMeta for the resulting resource.
     * @param fileStreamMethod  gRPC method returning <code>stream FileChunk</code>; the data backing this resource.
     * @param reactiveMethodRef A Reactive gRPC stub method reference to receive FileChunks. For consistency should be
     *                          the same method referenced by {@link #fileStreamMethod}. If null, a standard async stub
     *                          is used to feed the input stream.
     */
    public EoppFileStreamResource(FileMeta fileMeta, GrpcMethod<S, P, FileChunk> fileStreamMethod, @Nullable Function<Mono<P>, Flux<FileChunk>> reactiveMethodRef) {
        Preconditions.checkNotNull(fileMeta);
        this.fileMeta = fileMeta;
        this.fileStreamMethod = fileStreamMethod;
        this.reactiveMethodRef = reactiveMethodRef;
    }

    @Override
    public FileMeta getFileMeta() {
        return fileMeta;
    }

    @Override
    public boolean isCacheable() {
        return true;
    }

    @Override
    public boolean exists() {
        // FileMeta shouldn't exist for a non-existent file
        return true;
    }

    @Override
    public URL getURL() {
        throw new UnsupportedOperationException("gRPC URI may not be represented as a java.net.URL");
    }

    @Override
    public URI getURI() {
        return fileStreamMethod.toURI();
    }

    @Override
    public File getFile() throws IOException {
        throw new FileNotFoundException("gRPC stream resources may not be resolved as Files");
    }

    @Override
    public long lastModified() {
        return fileMeta.hasLastModified() ? fileMeta.getLastModified().getSeconds() : 0L;
    }

    @Override
    public Resource createRelative(String relativePath) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getFilename() {
        return fileMeta.getFilename();
    }

    @Override
    public String getDescription() {
        return "Grpc File Stream [" + fileMeta.getFilename() + "]";
    }

    @Override
    public InputStream getInputStream() throws IOException {
        @SuppressWarnings("java:S2095") // no need to close pos explicitly here as it is closed along with the returned pis
        // Set up a sink of data into which the gRPC StreamObserver<FileChunk> can write
        PipedOutputStream pos = new PipedOutputStream();
        // Pipe the sink stream to the caller
        PipedInputStream pis = new PipedInputStream(pos);

        if (reactiveMethodRef != null) {
            streamReactiveStub(pos);
        } else {
            streamAsyncStub(pos);
        }

        return pis;
    }

    private void streamReactiveStub(PipedOutputStream pos) throws IOException {
        log.debug("Receiving FileChunk stream as Flux from {}", fileStreamMethod);
        try (pos) {
            FileStreams.writeToStream(Mono.just(fileStreamMethod.getRequest()), reactiveMethodRef, pos);
        }
    }

    private void streamAsyncStub(PipedOutputStream pos) {
        log.debug("Receiving FileChunk stream with manual StreamObserver from {}", fileStreamMethod);

        StreamObserver<FileChunk> responseObserver = new FileChunkReceiver(pos);

        // Start receiving data to the connected PipedOutputStream
        ClientCalls.asyncServerStreamingCall(fileStreamMethod.getStub().getChannel().newCall(
                        fileStreamMethod.getMethodDescriptor(),
                        fileStreamMethod.getStub().getCallOptions()),
                fileStreamMethod.getRequest(),
                responseObserver);
    }

    /**
     * <p>A simple client to receive a stream of {@link FileChunk}s and send all data to an {@link OutputStream}.</p>
     * <p><strong>Does not handle back-pressure.</strong></p>
     */
    private static final class FileChunkReceiver implements StreamObserver<FileChunk> {
        private final OutputStream outputStream;

        private FileChunkReceiver(OutputStream outputStream) {
            this.outputStream = outputStream;
        }

        @Override
        public void onNext(FileChunk value) {
            try {
                value.getData().writeTo(outputStream);
            } catch (IOException e) {
                throw new FileStreamIOException(e);
            }
        }

        @Override
        public void onError(Throwable t) {
            closePipedOutputStream();
            throw new FileStreamIOException(t);
        }

        @Override
        public void onCompleted() {
            closePipedOutputStream();
        }

        private void closePipedOutputStream() {
            try {
                outputStream.close();
            } catch (IOException e) {
                throw new FileStreamIOException(e);
            }
        }
    }

}
