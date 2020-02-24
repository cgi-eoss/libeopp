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
import com.google.common.io.ByteStreams;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.List;

/**
 * <p>Specification of {@link ZipCombiningResource} with optional calculation of the resulting filesize and checksum,
 * making it suitable for integration with other libeopp functionality.</p>
 */
public class EoppZipCombiningResource extends ZipCombiningResource implements EoppResource {

    private final Instant lastModified;
    private final Long originalSize;
    private final Long zippedSize;
    private final String zipChecksum;

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
        super(contents);

        if (calculateDetailedProperties) {
            Instant lastModified = Instant.ofEpochMilli(0);
            long originalSize = 0;
            for (ZipResourceEntry entry : contents) {
                if (entry.getLastModified().toInstant().isAfter(lastModified)) {
                    lastModified = entry.getLastModified().toInstant();
                }
                if (entry.getResource() != null) {
                    originalSize += entry.getResource().contentLength();
                }
            }

            HashingCountingOutputStream hashingCountingOutputStream = new HashingCountingOutputStream(ByteStreams.nullOutputStream());
            try (InputStream inputStream = getInputStream()) {
                ByteStreams.copy(inputStream, hashingCountingOutputStream);
            } finally {
                hashingCountingOutputStream.close();
            }

            this.lastModified = lastModified;
            this.originalSize = originalSize;
            this.zippedSize = hashingCountingOutputStream.getCount();
            this.zipChecksum = hashingCountingOutputStream.checksum();
        } else {
            this.lastModified = Instant.now();
            this.originalSize = 0L;
            this.zippedSize = 0L;
            this.zipChecksum = null;
        }
    }

    @Override
    public long lastModified() throws IOException {
        return lastModified.toEpochMilli();
    }

    @Override
    public long contentLength() {
        return zippedSize;
    }

    @Override
    public FileMeta getFileMeta() {
        // no filename for dynamically-created resources
        return FileMeta.newBuilder()
                .setLastModified(Timestamps.timestampFromInstant(lastModified))
                .setSize(zippedSize)
                .setChecksum(zipChecksum)
                .build();
    }

    @Override
    public boolean isCacheable() {
        return false;
    }

    /**
     * @return The original file size of all entries in the zip file, i.e. the sum of all constituent resource
     * contentLengths.
     */
    public Long getOriginalSize() {
        return originalSize;
    }

}
