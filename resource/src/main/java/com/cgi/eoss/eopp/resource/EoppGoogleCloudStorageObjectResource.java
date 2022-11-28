/*
 *  Copyright 2022 The libeopp Team
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.cgi.eoss.eopp.resource;

import com.cgi.eoss.eopp.file.FileMeta;
import com.cgi.eoss.eopp.file.FileMetas;
import com.cgi.eoss.eopp.util.EoppHeaders;
import com.cgi.eoss.eopp.util.Lazy;
import com.cgi.eoss.eopp.util.Timestamps;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.channels.Channels;
import java.time.Instant;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.google.api.gax.rpc.StatusCode.Code.NOT_FOUND;

/**
 * <p>{@link EoppResource} implementation representing data in an Google Storage bucket.</p>
 * <p>If the libeopp application is working with its own Google Storage bucket for local storage,
 * this class may be extended to make {@link #isCacheable()} return <code>false</code> for local
 * resources.</p>
 */
public class EoppGoogleCloudStorageObjectResource implements EoppResource {

    private final Storage storage;
    private final String bucket;
    private final String name;
    private final Supplier<ResourceMetadataWrapper> metadata = Lazy.lazily(this::getGoogleStorageObjectMetadata);

    protected EoppGoogleCloudStorageObjectResource(Storage storage, String bucket, String name) {
        this.bucket = bucket;
        this.name = name;
        this.storage = storage;
    }

    protected InputStream doGetInputStream(String bucket, String name) {
        try {
            return Channels.newInputStream(storage.reader(BlobId.of(bucket, name)));
        } catch (Exception e) {
            throw new EoppResourceException("Failed to complete Cloud Storage resource GET request", e);
        }
    }

    protected Resource doCreateRelative(String bucket, String name) {
        return new EoppGoogleCloudStorageObjectResource(storage, bucket, name);
    }

    protected Optional<Blob> getObject(String bucket, String name) {
        try {
            // storage.get returns null for a not-found object
            return Optional.ofNullable(storage.get(BlobId.of(bucket, name)));
        } catch (Exception e) {
            throw new EoppResourceException("Failed to get Google Cloud Storage object", e);
        }
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
        return URI.create("gcs://" + bucket + "/" + name);
    }

    @Override
    public File getFile() throws IOException {
        throw new FileNotFoundException("Google Cloud Storage resources may not be resolved as Files");
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
        return doCreateRelative(bucket, StringUtils.applyRelativePath(name, relativePath));
    }

    @Override
    public String getFilename() {
        return metadata.get().getFilename();
    }

    @Override
    public String getDescription() {
        return "GoogleStorageObjectResource [" + getURI() + "]";
    }

    @Override
    public String toString() {
        return getDescription();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return doGetInputStream(bucket, name);
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

    protected ResourceMetadataWrapper getGoogleStorageObjectMetadata() {
        return getObject(bucket, name)
                .map(object -> {
                    ResourceMetadataWrapper.ResourceMetadataWrapperBuilder builder = ResourceMetadataWrapper.builder();

                    builder.exists(true);
                    builder.readable(true);

                    // Prefer the metadata attributes from the complete stored FileMeta, if it's available
                    Optional<FileMeta> fileMeta = Optional.ofNullable(object.getMetadata().get(EoppHeaders.FILE_META.getHeader()))
                            .map(FileMetas::fromBase64);

                    fileMeta.ifPresent(builder::fileMeta);

                    Stream.of(fileMeta.map(FileMeta::getLastModified).map(Timestamps::instantFromTimestamp).map(Instant::toEpochMilli),
                                    Optional.ofNullable(object.getUpdateTimeOffsetDateTime()).map(t -> t.toInstant().toEpochMilli()))
                            .filter(Optional::isPresent).map(Optional::get).findFirst()
                            .ifPresent(builder::lastModified);

                    Stream.of(fileMeta.map(FileMeta::getSize),
                                    Optional.ofNullable(object.getMetadata().get(EoppHeaders.PRODUCT_ARCHIVE_SIZE.getHeader())).map(Long::valueOf),
                                    Optional.of(object.getSize()))
                            .filter(Optional::isPresent).map(Optional::get).findFirst()
                            .ifPresent(builder::contentLength);

                    builder.filename(Stream.of(fileMeta.map(FileMeta::getFilename),
                                    Optional.ofNullable(object.getMetadata().get(EoppHeaders.PRODUCT_ARCHIVE_NAME.getHeader())),
                                    Optional.ofNullable(object.getContentDisposition()).flatMap(EoppHeaders.FILENAME_FROM_HTTP_HEADER))
                            .filter(Optional::isPresent).map(Optional::get).findFirst()
                            .orElse(StringUtils.getFilename(this.name)));

                    Stream.of(fileMeta.map(FileMeta::getChecksum),
                                    Optional.ofNullable(object.getMetadata().get(EoppHeaders.PRODUCT_ARCHIVE_CHECKSUM.getHeader())))
                            .filter(Optional::isPresent).map(Optional::get).findFirst()
                            .ifPresent(builder::checksum);

                    return builder.build();
                })
                .orElseGet(() -> ResourceMetadataWrapper.builder()
                        .exists(false)
                        .readable(false)
                        .fileMeta(FileMeta.getDefaultInstance())
                        .build());
    }

}
