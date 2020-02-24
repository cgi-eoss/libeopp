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

import java.util.Optional;

/**
 * <p>Internal convenience wrapper for common metadata fields which can be calculated from a resource supplier.</p>
 */
final class ResourceMetadataWrapper {
    private final boolean exists;
    private final boolean readable;
    private final long lastModified;
    private final String filename;
    private final long contentLength;
    private final Optional<String> checksum;

    ResourceMetadataWrapper(boolean exists, boolean readable, long lastModified, String filename, long contentLength, Optional<String> checksum) {
        this.exists = exists;
        this.readable = readable;
        this.lastModified = lastModified;
        this.filename = filename;
        this.contentLength = contentLength;
        this.checksum = checksum;
    }

    public static ResourceMetadataWrapperBuilder builder() {
        return new ResourceMetadataWrapperBuilder();
    }

    public boolean isExists() {
        return this.exists;
    }

    public boolean isReadable() {
        return this.readable;
    }

    public long getLastModified() {
        return this.lastModified;
    }

    public String getFilename() {
        return this.filename;
    }

    public long getContentLength() {
        return this.contentLength;
    }

    public Optional<String> getChecksum() {
        return this.checksum;
    }

    public static class ResourceMetadataWrapperBuilder {
        private boolean exists = false;
        private boolean readable = false;
        private long lastModified = 0L;
        private long contentLength = 0L;
        private String filename = null;
        private String checksum = null;

        ResourceMetadataWrapperBuilder() {
        }

        public ResourceMetadataWrapper.ResourceMetadataWrapperBuilder exists(boolean exists) {
            this.exists = exists;
            return this;
        }

        public ResourceMetadataWrapper.ResourceMetadataWrapperBuilder readable(boolean readable) {
            this.readable = readable;
            return this;
        }

        public ResourceMetadataWrapper.ResourceMetadataWrapperBuilder lastModified(long lastModified) {
            this.lastModified = lastModified;
            return this;
        }

        public ResourceMetadataWrapper.ResourceMetadataWrapperBuilder contentLength(long contentLength) {
            this.contentLength = contentLength;
            return this;
        }

        public ResourceMetadataWrapper.ResourceMetadataWrapperBuilder filename(String filename) {
            this.filename = filename;
            return this;
        }

        public ResourceMetadataWrapper.ResourceMetadataWrapperBuilder checksum(String checksum) {
            this.checksum = checksum;
            return this;
        }

        public ResourceMetadataWrapper build() {
            return new ResourceMetadataWrapper(exists, readable, lastModified, filename, contentLength, Optional.ofNullable(checksum));
        }
    }
}