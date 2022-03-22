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

import com.azure.storage.blob.models.BlobProperties;
import com.cgi.eoss.eopp.file.FileMeta;
import com.cgi.eoss.eopp.file.FileMetas;
import com.cgi.eoss.eopp.util.EoppHeaders;
import com.cgi.eoss.eopp.util.Lazy;
import com.cgi.eoss.eopp.util.Timestamps;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

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
 * <p>An {@link EoppResource} representing data in an Azure Blob Storage container.</p>
 */
abstract class BaseAzureBlobResource implements EoppResource {

    private final String container;
    private final String name;
    private final Supplier<ResourceMetadataWrapper> metadata = Lazy.lazily(this::getAzureBlobMetadata);

    protected BaseAzureBlobResource(String container, String name) {
        this.container = container;
        this.name = name;
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
        return doGetURI(container, name);
    }

    @Override
    public File getFile() throws IOException {
        throw new FileNotFoundException("Azure Blob resources may not be resolved as Files");
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
        return doCreateRelative(container, StringUtils.applyRelativePath(name, relativePath));
    }

    @Override
    public String getFilename() {
        return metadata.get().getFilename();
    }

    @Override
    public String getDescription() {
        return "AzureBlobResource [" + getURI() + "]";
    }

    @Override
    public String toString() {
        return getDescription();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return doGetInputStream(container, name);
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

    protected ResourceMetadataWrapper getAzureBlobMetadata() {
        return getBlobProperties(container, name)
                .map(properties -> {
                    ResourceMetadataWrapper.ResourceMetadataWrapperBuilder builder = ResourceMetadataWrapper.builder();

                    builder.exists(true);
                    builder.readable(true);

                    // Prefer the metadata attributes from the complete stored FileMeta, if it's available
                    Optional<FileMeta> fileMeta = Optional.ofNullable(properties.getMetadata().get(azureHeader(EoppHeaders.FILE_META)))
                            .map(FileMetas::fromBase64);

                    fileMeta.ifPresent(builder::fileMeta);

                    Stream.of(fileMeta.map(FileMeta::getLastModified).map(Timestamps::instantFromTimestamp).map(Instant::toEpochMilli),
                                    Optional.ofNullable(properties.getLastModified()).map(t -> t.toInstant().toEpochMilli()))
                            .filter(Optional::isPresent).map(Optional::get).findFirst()
                            .ifPresent(builder::lastModified);

                    Stream.of(fileMeta.map(FileMeta::getSize),
                                    Optional.ofNullable(properties.getMetadata().get(azureHeader(EoppHeaders.PRODUCT_ARCHIVE_SIZE))).map(Long::valueOf),
                                    Optional.of(properties.getBlobSize()))
                            .filter(Optional::isPresent).map(Optional::get).findFirst()
                            .ifPresent(builder::contentLength);

                    builder.filename(Stream.of(fileMeta.map(FileMeta::getFilename),
                                    Optional.ofNullable(properties.getMetadata().get(azureHeader(EoppHeaders.PRODUCT_ARCHIVE_NAME))),
                                    Optional.ofNullable(properties.getContentDisposition()).flatMap(EoppHeaders.FILENAME_FROM_HTTP_HEADER))
                            .filter(Optional::isPresent).map(Optional::get).findFirst()
                            .orElse(StringUtils.getFilename(this.name)));

                    Stream.of(fileMeta.map(FileMeta::getChecksum),
                                    Optional.ofNullable(properties.getMetadata().get(azureHeader(EoppHeaders.PRODUCT_ARCHIVE_CHECKSUM))))
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

    protected abstract URI doGetURI(String container, String name);

    protected abstract InputStream doGetInputStream(String container, String name);

    protected abstract Optional<BlobProperties> getBlobProperties(String container, String name);

    protected abstract Resource doCreateRelative(String container, String name);

    private String azureHeader(EoppHeaders eoppHeader) {
        return eoppHeader.getHeader().replace('-', '_');
    }

}
