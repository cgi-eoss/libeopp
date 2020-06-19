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
import com.cgi.eoss.eopp.util.EoppHeaders;
import com.cgi.eoss.eopp.util.Lazy;
import com.cgi.eoss.eopp.util.Timestamps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.util.StringUtils;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * <p>{@link EoppResource} implementation representing data in an S3 (or S3-compatible) bucket.</p>
 * <p>If the libeopp application is working with its own S3 bucket for local storage, this class may be extended to make
 * {@link #isCacheable()} return <code>false</code> for local resources.</p>
 */
public class EoppS3ObjectResource implements EoppResource {
    private static final Logger log = LoggerFactory.getLogger(EoppS3ObjectResource.class);

    private static final DataBufferFactory DEFAULT_DATA_BUFFER_FACTORY = new DefaultDataBufferFactory(true);

    private final S3AsyncClient s3AsyncClient;
    private final String bucket;
    private final String key;
    private final DataBufferFactory dataBufferFactory;
    private Supplier<ResourceMetadataWrapper> metadata = Lazy.lazily(this::getS3ObjectMetadata);

    public EoppS3ObjectResource(S3AsyncClient s3AsyncClient, String bucket, String key) {
        this.s3AsyncClient = s3AsyncClient;
        this.bucket = bucket;
        this.key = key;
        this.dataBufferFactory = DEFAULT_DATA_BUFFER_FACTORY;
    }

    @Override
    public boolean exists() {
        return metadata.get().isExists();
    }

    @Override
    public boolean isReadable() {
        return metadata.get().isReadable();
    }

    @Override
    public URL getURL() throws IOException {
        return getURI().toURL();
    }

    @Override
    public URI getURI() {
        return URI.create("s3://" + bucket + "/" + key);
    }

    @Override
    public File getFile() throws IOException {
        throw new FileNotFoundException("S3 resources may not be resolved as Files");
    }

    @Override
    public FileMeta getFileMeta() {
        FileMeta.Builder fileMeta = FileMeta.newBuilder()
                .setFilename(this.getFilename())
                .setSize(this.contentLength())
                .setLastModified(Timestamps.timestampFromInstant(Instant.ofEpochMilli(this.lastModified())))
                .setExecutable(false);
        metadata.get().getChecksum().ifPresent(fileMeta::setChecksum);
        return fileMeta.build();
    }

    @Override
    public boolean isCacheable() {
        return true;
    }

    @Override
    public long contentLength() {
        return metadata.get().getContentLength();
    }

    @Override
    public long lastModified() {
        return metadata.get().getLastModified();
    }

    @Override
    public Resource createRelative(String relativePath) {
        return new EoppS3ObjectResource(s3AsyncClient, bucket, StringUtils.applyRelativePath(key, relativePath));
    }

    @Override
    public String getFilename() {
        return metadata.get().getFilename();
    }

    @Override
    public String getDescription() {
        return "S3Object [" + getURI() + "]";
    }

    @Override
    public InputStream getInputStream() throws IOException {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        GetObjectInputStream responseTransformer = new GetObjectInputStream(dataBufferFactory);

        try {
            return s3AsyncClient.getObject(request, responseTransformer)
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            throw new IOException(e);
        }
    }

    private ResourceMetadataWrapper getS3ObjectMetadata() {
        return Mono.fromFuture(s3AsyncClient.headObject(HeadObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build()))
                .map(Optional::of)
                .onErrorReturn(NoSuchKeyException.class, Optional.empty())
                .map(response -> {
                    ResourceMetadataWrapper.ResourceMetadataWrapperBuilder builder = ResourceMetadataWrapper.builder();

                    if (response.isPresent()) {
                        builder.exists(true);
                        builder.readable(true);
                        builder.lastModified(response.get().lastModified().toEpochMilli());
                        Stream.of(Optional.ofNullable(response.get().metadata().get(EoppHeaders.PRODUCT_ARCHIVE_SIZE.getHeader())).map(Long::valueOf),
                                Optional.ofNullable(response.get().contentLength()))
                                .filter(Optional::isPresent).map(Optional::get).findFirst()
                                .ifPresent(builder::contentLength);
                        builder.filename(Stream.of(
                                Optional.ofNullable(response.get().metadata().get(EoppHeaders.PRODUCT_ARCHIVE_NAME.getHeader())),
                                Optional.ofNullable(response.get().contentDisposition()).flatMap(EoppHeaders.FILENAME_FROM_HTTP_HEADER))
                                .filter(Optional::isPresent).map(Optional::get).findFirst()
                                .orElse(StringUtils.getFilename(key)));
                        Optional.ofNullable(response.get().metadata().get(EoppHeaders.PRODUCT_ARCHIVE_CHECKSUM.getHeader()))
                                .ifPresent(builder::checksum);
                    }

                    return builder.build();
                })
                .block();
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
