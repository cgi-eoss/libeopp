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
import com.google.common.base.Preconditions;
import com.google.common.net.HttpHeaders;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * <p>{@link EoppResource} implementation for retrieving data from HTTP(S) sources.</p>
 */
public class EoppOkHttpResource implements EoppResource {

    private static final Logger log = LoggerFactory.getLogger(EoppOkHttpResource.class);

    private final OkHttpClient httpClient;
    private final HttpUrl url;
    private final Supplier<ResourceMetadataWrapper> metadata = Lazy.lazily(this::getHttpResourceMetadata);

    private int remainingRetries = 3;

    public EoppOkHttpResource(OkHttpClient httpClient, HttpUrl url) {
        this.httpClient = httpClient;
        this.url = url;
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
    public boolean shouldRetry(Throwable throwable) {
        if (throwable instanceof SocketTimeoutException && remainingRetries > 0) {
            remainingRetries--;
            return true;
        }
        return false;
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
    public boolean isOpen() {
        return false;
    }

    @Override
    public URL getURL() {
        return url.url();
    }

    @Override
    public URI getURI() {
        return url.uri();
    }

    @Override
    public File getFile() throws IOException {
        throw new FileNotFoundException("HTTP resources may not be resolved as Files");
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
        if (relativePath.startsWith("/")) {
            relativePath = relativePath.substring(1);
        }
        return new EoppOkHttpResource(httpClient, Objects.requireNonNull(url.resolve(relativePath)));
    }

    @Override
    public String getFilename() {
        return metadata.get().getFilename();
    }

    @Override
    public String getDescription() {
        return "HttpUrl [" + url + "]";
    }

    @Override
    public InputStream getInputStream() throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            Preconditions.checkState(response.isSuccessful(), "Did not receive successful HTTP response: %s", response);
            return Objects.requireNonNull(response.body()).byteStream();
        } catch (Exception e) {
            Optional.ofNullable(response).ifPresent(Response::close);
            throw e;
        }
    }

    protected ResourceMetadataWrapper getHttpResourceMetadata() {
        // Make a HEAD request to get metadata only
        Request request = new Request.Builder().url(url).head().build();

        ResourceMetadataWrapper.ResourceMetadataWrapperBuilder builder = ResourceMetadataWrapper.builder();

        try (Response response = httpClient.newCall(request).execute()) {
            log.trace("Received HEAD response: {}", response);

            if (response.isSuccessful()) {
                builder.exists(true);
                builder.readable(true);

                // Prefer the metadata attributes from the complete stored FileMeta, if it's available
                Optional<FileMeta> fileMeta = Optional.ofNullable(response.header(EoppHeaders.FILE_META.getHeader()))
                        .map(FileMetas::fromBase64);

                fileMeta.ifPresent(builder::fileMeta);

                Stream.of(fileMeta.map(FileMeta::getLastModified).map(Timestamps::instantFromTimestamp).map(Instant::toEpochMilli),
                        Optional.ofNullable(response.header(HttpHeaders.LAST_MODIFIED)).map(EoppHeaders::parseInstant).map(Instant::toEpochMilli))
                        .filter(Optional::isPresent).map(Optional::get).findFirst()
                        .ifPresent(builder::lastModified);

                Stream.of(fileMeta.map(FileMeta::getSize),
                        Optional.ofNullable(response.header(EoppHeaders.PRODUCT_ARCHIVE_SIZE.getHeader())).map(Long::valueOf),
                        Optional.ofNullable(response.header(HttpHeaders.CONTENT_LENGTH)).map(Long::valueOf))
                        .filter(Optional::isPresent).map(Optional::get).findFirst()
                        .ifPresent(builder::contentLength);

                builder.filename(Stream.of(fileMeta.map(FileMeta::getFilename),
                        Optional.ofNullable(response.header(EoppHeaders.PRODUCT_ARCHIVE_NAME.getHeader())),
                        Optional.ofNullable(response.header(HttpHeaders.CONTENT_DISPOSITION)).flatMap(EoppHeaders.FILENAME_FROM_HTTP_HEADER))
                        .filter(Optional::isPresent).map(Optional::get).findFirst()
                        .orElse(StringUtils.getFilename(url.encodedPath())));

                Stream.of(fileMeta.map(FileMeta::getChecksum),
                        Optional.ofNullable(response.header(EoppHeaders.PRODUCT_ARCHIVE_CHECKSUM.getHeader())))
                        .filter(Optional::isPresent).map(Optional::get).findFirst()
                        .ifPresent(builder::checksum);
            }
        } catch (IOException e) {
            log.warn("Failed to HEAD resource at {}", url, e);
        }

        return builder.build();
    }

}
