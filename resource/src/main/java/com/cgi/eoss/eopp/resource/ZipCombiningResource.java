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

import com.google.common.io.ByteStreams;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import org.springframework.util.unit.DataSize;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.file.attribute.FileTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * <p>A meta-resource which collects other Resource instances into a zip stream.</p>
 * <p>The zip is assembled on demand when {@link #getInputStream()} is called.</p>
 */
public class ZipCombiningResource extends AbstractResource {

    public static final int DEFAULT_BUFFER_SIZE = Math.toIntExact(DataSize.ofMegabytes(1).toBytes());
    public static final int DEFAULT_COMPRESSION_LEVEL = Deflater.DEFAULT_COMPRESSION;

    private final List<ZipResourceEntry> contents;
    private final int zipBufferSize;
    private final int compressionLevel;

    /**
     * <p>Create a ZipCombiningResource with the given contents and default buffering and compression parameters.</p>
     *
     * @param contents The zip resource entries.
     */
    public ZipCombiningResource(List<ZipResourceEntry> contents) {
        this(contents, DEFAULT_BUFFER_SIZE, DEFAULT_COMPRESSION_LEVEL);
    }

    /**
     * <p>Create a ZipCombiningResource with the given contents and buffering and compression parameters.</p>
     *
     * @param contents         The zip resource entries.
     * @param zipBufferSize    The buffer size used for the piped ZipOutputStream.
     * @param compressionLevel The zip compression level. See {@link Deflater} for valid values.
     */
    public ZipCombiningResource(List<ZipResourceEntry> contents, int zipBufferSize, int compressionLevel) {
        this.contents = contents;
        this.zipBufferSize = zipBufferSize;
        this.compressionLevel = compressionLevel;
    }

    @Override
    public String getDescription() {
        return "ZipFile (compression: " + compressionLevel + ") " +
                "[ " + contents.stream().map(it -> it.resource).filter(Objects::nonNull).map(Resource::getDescription).collect(Collectors.joining(",")) + " ]";
    }

    @Override
    public InputStream getInputStream() throws IOException {
        PipedOutputStream pos = new PipedOutputStream();
        PipedInputStream pis = new PipedInputStream(pos);

        // Use a thread to asynchronously write the zipped resources to the
        // given PipedOutputStream. This thread ensures that the reading from
        // the associated PipedInputStream is not blocked indefinitely while
        // the zip is written to the output buffer but nothing is consuming
        // from the associated pipe. Such behaviour would be fine if the total
        // output size is smaller than the buffer, but this is not guaranteed.
        CompletableFuture.runAsync(() -> {
            try (ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(pos, zipBufferSize))) {
                zos.setLevel(compressionLevel);

                for (ZipResourceEntry fe : contents) {
                    ZipEntry ze = new ZipEntry(fe.path);
                    ze.setLastModifiedTime(fe.lastModified);
                    zos.putNextEntry(ze);
                    if (fe.resource != null) {
                        try (InputStream inputStream = fe.resource.getInputStream()) {
                            ByteStreams.copy(inputStream, zos);
                        } finally {
                            zos.closeEntry();
                        }
                    }
                }
            } catch (IOException e) {
                throw new EoppResourceException(e);
            }
        });

        return pis;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public static final class ZipResourceEntry {
        private final String path;
        private final FileTime lastModified;

        private final Resource resource;

        public ZipResourceEntry(String path, FileTime lastModified) {
            this(path, lastModified, null);
        }

        public ZipResourceEntry(String path, Resource resource) {
            this.path = path;
            this.resource = resource;
            this.lastModified = safeGetFileTime(resource);
        }

        public ZipResourceEntry(String path, FileTime lastModified, Resource resource) {
            this.path = path;
            this.lastModified = lastModified;
            this.resource = resource;
        }

        private FileTime safeGetFileTime(Resource resource) {
            try {
                return FileTime.fromMillis(resource.lastModified());
            } catch (IOException ignored) {
                return FileTime.fromMillis(0L);
            }
        }

        public String getPath() {
            return path;
        }

        public FileTime getLastModified() {
            return lastModified;
        }

        public Resource getResource() {
            return resource;
        }
    }

}
