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
import com.cgi.eoss.eopp.util.Lazy;
import com.cgi.eoss.eopp.util.Timestamps;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.RequestPayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.time.Instant;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * <p>An {@link EoppResource} representing data in an S3 (or S3-compatible) bucket.</p>
 */
abstract class BaseS3ObjectResource implements EoppResource {

    private final String bucket;
    private final String key;
    private final boolean requesterPays;
    private final Supplier<com.cgi.eoss.eopp.resource.ResourceMetadataWrapper> metadata = Lazy.lazily(this::getS3ObjectMetadata);

    BaseS3ObjectResource(String bucket, String key) {
        this(bucket, key, false);
    }

    protected BaseS3ObjectResource(String bucket, String key, boolean requesterPays) {
        this.bucket = bucket;
        this.key = key;
        this.requesterPays = requesterPays;
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
        return metadata.get().getFileMeta();
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
        return doCreateRelative(bucket, StringUtils.applyRelativePath(key, relativePath));
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
    public String toString() {
        return getDescription();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        GetObjectRequest.Builder request = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key);
        if (requesterPays) {
            request.requestPayer(RequestPayer.REQUESTER);
        }
        return doGetInputStream(request.build());
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return (this == other || (other instanceof Resource &&
                ((Resource) other).getDescription().equals(getDescription())));
    }

    @Override
    public int hashCode() {
        return getDescription().hashCode();
    }

    protected com.cgi.eoss.eopp.resource.ResourceMetadataWrapper getS3ObjectMetadata() {
        HeadObjectRequest.Builder request = HeadObjectRequest.builder()
                .bucket(bucket)
                .key(key);
        if (requesterPays) {
            request.requestPayer(RequestPayer.REQUESTER);
        }
        return headObject(request.build())
                .map(response -> {

                    com.cgi.eoss.eopp.resource.ResourceMetadataWrapper.ResourceMetadataWrapperBuilder builder = com.cgi.eoss.eopp.resource.ResourceMetadataWrapper.builder();

                    builder.exists(true);
                    builder.readable(true);

                    // Prefer the metadata attributes from the complete stored FileMeta, if it's available
                    Optional<FileMeta> fileMeta = Optional.ofNullable(response.metadata().get(EoppHeaders.FILE_META.getHeader().toLowerCase()))
                            .map(FileMetas::fromBase64);

                    fileMeta.ifPresent(builder::fileMeta);

                    Stream.of(fileMeta.map(FileMeta::getLastModified).map(Timestamps::instantFromTimestamp).map(Instant::toEpochMilli),
                            Optional.ofNullable(response.lastModified()).map(Instant::toEpochMilli))
                            .filter(Optional::isPresent).map(Optional::get).findFirst()
                            .ifPresent(builder::lastModified);

                    Stream.of(fileMeta.map(FileMeta::getSize),
                            Optional.ofNullable(response.metadata().get(EoppHeaders.PRODUCT_ARCHIVE_SIZE.getHeader())).map(Long::valueOf),
                            Optional.ofNullable(response.metadata().get(EoppHeaders.PRODUCT_ARCHIVE_SIZE.getHeader().toLowerCase())).map(Long::valueOf),
                            Optional.ofNullable(response.contentLength()))
                            .filter(Optional::isPresent).map(Optional::get).findFirst()
                            .ifPresent(builder::contentLength);

                    builder.filename(Stream.of(fileMeta.map(FileMeta::getFilename),
                            Optional.ofNullable(response.metadata().get(EoppHeaders.PRODUCT_ARCHIVE_NAME.getHeader())),
                            Optional.ofNullable(response.metadata().get(EoppHeaders.PRODUCT_ARCHIVE_NAME.getHeader().toLowerCase())),
                            Optional.ofNullable(response.contentDisposition()).flatMap(EoppHeaders.FILENAME_FROM_HTTP_HEADER))
                            .filter(Optional::isPresent).map(Optional::get).findFirst()
                            .orElse(StringUtils.getFilename(this.key)));

                    Stream.of(fileMeta.map(FileMeta::getChecksum),
                            Optional.ofNullable(response.metadata().get(EoppHeaders.PRODUCT_ARCHIVE_CHECKSUM.getHeader())),
                            Optional.ofNullable(response.metadata().get(EoppHeaders.PRODUCT_ARCHIVE_CHECKSUM.getHeader().toLowerCase())))
                            .filter(Optional::isPresent).map(Optional::get).findFirst()
                            .ifPresent(builder::checksum);

                    return builder.build();
                })
                .orElseGet(() -> com.cgi.eoss.eopp.resource.ResourceMetadataWrapper.builder()
                        .exists(false)
                        .readable(false)
                        .fileMeta(FileMeta.getDefaultInstance())
                        .build());
    }

    protected abstract InputStream doGetInputStream(GetObjectRequest request) throws IOException;

    protected abstract Optional<HeadObjectResponse> headObject(HeadObjectRequest headObjectRequest);

    protected abstract Resource doCreateRelative(String bucket, String key);

}
