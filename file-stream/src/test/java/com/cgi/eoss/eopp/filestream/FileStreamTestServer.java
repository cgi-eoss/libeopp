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
import com.google.common.base.Stopwatch;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;

import java.net.URI;

public class FileStreamTestServer extends FileStreamTestServerGrpc.FileStreamTestServerImplBase {
    private static final Logger log = LoggerFactory.getLogger(FileStreamTestServer.class);

    @Override
    public void getFile(GetFileParam request, StreamObserver<FileChunk> responseObserver) {
        String filename = StringUtils.getFilename(request.getUri());
        try {
            UrlResource resource = new UrlResource(URI.create(request.getUri()));
            long size = resource.contentLength();

            Stopwatch stopwatch = Stopwatch.createUnstarted();
            FileStreams.create(resource)
                    .doOnSubscribe(subscription -> {
                        log.info("Serving output file: {} ({} bytes)", filename, size);
                        stopwatch.start();
                    })
                    .subscribe(
                            responseObserver::onNext,
                            e -> {
                                log.error("Failed serving output file: {}", filename, e);
                                responseObserver.onError(Status.UNKNOWN.withDescription("Error returning product as FileStream").withCause(e).asException());
                            },
                            () -> {
                                log.info("Served output file {} ({} bytes) in {}", filename, size, stopwatch.stop().elapsed());
                                responseObserver.onCompleted();
                            }
                    );
        } catch (Exception e) {
            log.error("Failed serving output file: {}", filename, e);
            responseObserver.onError(Status.UNKNOWN.withDescription("Error returning product as FileStream").withCause(e).asException());
        }
    }
}
