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
import com.cgi.eoss.eopp.util.HashingCountingOutputStream;
import com.cgi.eoss.eopp.util.Timestamps;
import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * <p>Specification of {@link ZipCombiningResource} with optional calculation of the resulting filesize and checksum,
 * making it suitable for integration with other libeopp functionality.</p>
 */
public class EoppZipCombiningResource extends ZipCombiningResource implements EoppResource {

    private final Instant lastModified;
    private final Long originalSize;
    private final Long zippedSize;
    private final String zipChecksum;
    private final String filename;

    /**
     * <p>Create a new zipping meta-resource with the given contents, optionally calculating the resulting zip checksum
     * and filesize.</p>
     *
     * @param contents                    The zip resource entries.
     * @param calculateDetailedProperties If true, all zip entries are fully resolved in order to create the resulting
     *                                    zip to capture the final zip file size and checksum. This may be time-
     *                                    consuming for resources which resolve slowly.
     */
    public EoppZipCombiningResource(List<ZipResourceEntry> contents, boolean calculateDetailedProperties) throws IOException {
        this(contents, null, calculateDetailedProperties);
    }

    /**
     * <p>Create a new zipping meta-resource with the given contents and filename, optionally calculating the resulting
     * zip checksum and filesize.</p>
     *
     * @param contents                    The zip resource entries.
     * @param filename                    The filename for this resource, reported by {@link Resource#getFilename()}.
     * @param calculateDetailedProperties If true, all zip entries are fully resolved in order to create the resulting
     *                                    zip to capture the final zip file size and checksum. This may be time-
     *                                    consuming for resources which resolve slowly.
     */
    public EoppZipCombiningResource(List<ZipResourceEntry> contents, String filename, boolean calculateDetailedProperties) throws IOException {
        this(contents, filename, calculateDetailedProperties, DEFAULT_BUFFER_SIZE, DEFAULT_COMPRESSION_LEVEL);
    }

    /**
     * <p>Create a new zipping meta-resource with the given contents, optionally calculating the resulting zip checksum
     * and filesize.</p>
     *
     * @param contents                    The zip resource entries.
     * @param filename                    The filename for this resource, reported by {@link Resource#getFilename()}.
     * @param calculateDetailedProperties If true, all zip entries are fully resolved in order to create the resulting
     *                                    zip to capture the final zip file size and checksum. This may be time-
     *                                    consuming for resources which resolve slowly.
     */
    public EoppZipCombiningResource(List<ZipResourceEntry> contents, String filename, boolean calculateDetailedProperties, int bufferSize, int compressionLevel) throws IOException {
        super(contents, bufferSize, compressionLevel);

        this.filename = filename;

        if (calculateDetailedProperties) {
            Instant resourceLastModified = Instant.ofEpochMilli(0);
            long resourceOriginalSize = 0;
            for (ZipResourceEntry entry : contents) {
                if (entry.getLastModified().toInstant().isAfter(resourceLastModified)) {
                    resourceLastModified = entry.getLastModified().toInstant();
                }
                if (entry.getResource() != null) {
                    resourceOriginalSize += entry.getResource().contentLength();
                }
            }

            HashingCountingOutputStream hashingCountingOutputStream = new HashingCountingOutputStream(ByteStreams.nullOutputStream());
            try (hashingCountingOutputStream; InputStream inputStream = getInputStream()) {
                ByteStreams.copy(inputStream, hashingCountingOutputStream);
            }

            this.lastModified = resourceLastModified;
            this.originalSize = resourceOriginalSize;
            this.zippedSize = hashingCountingOutputStream.getCount();
            this.zipChecksum = hashingCountingOutputStream.checksum();
        } else {
            this.lastModified = Instant.now();
            this.originalSize = -1L;
            this.zippedSize = -1L;
            this.zipChecksum = "";
        }
    }

    @Override
    public long lastModified() {
        return lastModified.toEpochMilli();
    }

    @Override
    public long contentLength() {
        return zippedSize;
    }

    @Override
    public FileMeta getFileMeta() {
        FileMeta.Builder builder = FileMeta.newBuilder()
                .setLastModified(Timestamps.timestampFromInstant(lastModified))
                .setSize(zippedSize)
                .setChecksum(zipChecksum);
        Optional.ofNullable(Strings.emptyToNull(filename)).ifPresent(builder::setFilename);
        return builder.build();
    }

    @Override
    public boolean isCacheable() {
        return false;
    }

    @Override
    public String getFilename() {
        return filename;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * @return The original file size of all entries in the zip file, i.e. the sum of all constituent resource
     * contentLengths.
     */
    public Long getOriginalSize() {
        return originalSize;
    }

}
