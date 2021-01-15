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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * <p>{@link EoppResource} implementation representing data in an S3 (or S3-compatible) bucket.</p>
 * <p>If the libeopp application is working with its own S3 bucket for local storage, this class may be extended to make
 * {@link #isCacheable()} return <code>false</code> for local resources.</p>
 * <p><strong>Note:</strong> The AWS SDK v2 {@link S3AsyncClient} can be flaky. {@link EoppS3ObjectResource} using
 * {@link software.amazon.awssdk.services.s3.S3Client} is recommended.</p>
 *
 * @see EoppS3ObjectResource
 */
public class EoppS3ObjectAsyncResource extends BaseS3ObjectResource implements EoppResource {
    private static final Logger log = LoggerFactory.getLogger(EoppS3ObjectAsyncResource.class);

    private static final DataBufferFactory DEFAULT_DATA_BUFFER_FACTORY = new DefaultDataBufferFactory(true);

    private final S3AsyncClient s3AsyncClient;
    private final DataBufferFactory dataBufferFactory;

    public EoppS3ObjectAsyncResource(S3AsyncClient s3AsyncClient, String bucket, String key) {
        super(bucket, key);
        this.s3AsyncClient = s3AsyncClient;
        this.dataBufferFactory = DEFAULT_DATA_BUFFER_FACTORY;
    }

    @Override
    protected Resource doCreateRelative(String bucket, String key) {
        return new EoppS3ObjectAsyncResource(s3AsyncClient, bucket, key);
    }

    @Override
    protected InputStream doGetInputStream(GetObjectRequest request) throws IOException {
        GetObjectInputStream responseTransformer = new GetObjectInputStream(dataBufferFactory);

        try {
            return s3AsyncClient.getObject(request, responseTransformer)
                    .get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException(e);
        } catch (ExecutionException e) {
            throw new IOException(e);
        }
    }

    @Override
    protected Optional<HeadObjectResponse> headObject(HeadObjectRequest headObjectRequest) {
        try {
            return Optional.of(s3AsyncClient.headObject(headObjectRequest).get());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new EoppResourceException("S3 resource HEAD request interrupted");
        } catch (ExecutionException e) {
            if (e.getCause() instanceof NoSuchKeyException) {
                return Optional.empty();
            } else {
                throw new EoppResourceException("Failed to complete S3 resource HEAD request", e.getCause());
            }
        }
    }

    /**
     * <p>Transforms the {@link SdkPublisher}&lt;{@link ByteBuffer}&gt; from the S3 SDK into an InputStream.</p>
     */
    private static class GetObjectInputStream implements AsyncResponseTransformer<GetObjectResponse, InputStream> {
        private PipedOutputStream pipedOutputStream;
        private CompletableFuture<InputStream> completableFuture;
        private DataBufferFactory dataBufferFactory;

        public GetObjectInputStream(DataBufferFactory dataBufferFactory) {
            this.dataBufferFactory = dataBufferFactory;
        }

        @Override
        public CompletableFuture<InputStream> prepare() {
            pipedOutputStream = new PipedOutputStream();
            completableFuture = Mono.just(pipedOutputStream)
                    .map(src -> {
                        try {
                            return new PipedInputStream(src);
                        } catch (IOException e) {
                            throw Exceptions.propagate(e);
                        }
                    })
                    .cast(InputStream.class)
                    .toFuture();
            return completableFuture;
        }

        @Override
        public void onResponse(GetObjectResponse getObjectResponse) {
            log.trace("Received GetObjectResponse: {}", getObjectResponse);
        }

        @Override
        public void onStream(SdkPublisher<ByteBuffer> publisher) {
            Flux<DataBuffer> dataBufferFlux = Flux.from(publisher)
                    .doOnSubscribe(subscription -> log.trace("Subscribing to GetObjectResponse data stream"))
                    .map(dataBufferFactory::wrap);
            DataBufferUtils
                    .write(dataBufferFlux, pipedOutputStream)
                    .doFinally(type -> {
                        try {
                            pipedOutputStream.close();
                        } catch (IOException e) {
                            throw Exceptions.propagate(e);
                        }
                    })
                    .subscribe(DataBufferUtils.releaseConsumer());
        }

        @Override
        public void exceptionOccurred(Throwable throwable) {
            completableFuture.completeExceptionally(throwable);
        }
    }

}
