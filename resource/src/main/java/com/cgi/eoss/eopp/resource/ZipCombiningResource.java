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

import org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.parallel.InputStreamSupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.Resource;
import org.springframework.util.unit.DataSize;

import java.io.BufferedOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;

/**
 * <p>A meta-resource which collects other Resource instances into a zip stream.</p>
 * <p>The zip is assembled on demand when {@link #getInputStream()} is called.</p>
 */
public class ZipCombiningResource extends AbstractResource {

    private static final Logger log = LoggerFactory.getLogger(ZipCombiningResource.class);

    private static final ExecutorService ZIP_EXECUTOR = Executors.newVirtualThreadPerTaskExecutor();

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
               "[ " + contents.stream().map(ZipResourceEntry::getResource).filter(Objects::nonNull).map(Resource::getDescription).collect(Collectors.joining(",")) + " ]";
    }

    @Override
    public boolean exists() {
        return true;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        @SuppressWarnings("java:S2095") // no need to close pos explicitly here as it is closed along with the returned pis
        PipedOutputStream pos = new PipedOutputStream();
        PipedInputStream pis = new PipedInputStream(pos);

        // Downloads inputstreams in parallel, then streams to the zip outputstream from those.
        // Note that this uses a `FileBasedScatterGatherBackingStore` backed by temp files.
        ParallelScatterZipCreator scatterZipCreator = new ParallelScatterZipCreator();

        // prepare the ZipArchiveEntries, the source streams will be resolved when calling scatterZipCreator#writeTo
        contents.stream()
                .map(this::buildZipArchiveEntry)
                .forEach(entry -> scatterZipCreator.addArchiveEntry(entry.zipArchiveEntry, entry.inputStreamSupplier));

        // Use a thread to asynchronously write the zipped resources to the
        // given PipedOutputStream. This thread ensures that the reading from
        // the associated PipedInputStream is not blocked indefinitely while
        // the zip is written to the output buffer but nothing is consuming
        // from the associated pipe. Such behaviour would be fine if the total
        // output size is smaller than the buffer, but this is not guaranteed.
        AtomicReference<EoppResourceException> exRef = new AtomicReference<>();
        CompletableFuture.runAsync(() -> {
            try (ZipArchiveOutputStream zos = new ZipArchiveOutputStream(new BufferedOutputStream(pos, zipBufferSize))) {
                zos.setUseZip64(Zip64Mode.Always);
                zos.setLevel(compressionLevel);

                log.trace("Beginning write to ZipArchiveOutputStream");
                scatterZipCreator.writeTo(zos);
                zos.finish();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                exRef.set(new EoppResourceException(e));
                throw exRef.get();
            } catch (EoppResourceException e) {
                exRef.set(e);
                throw new CompletionException(e);
            } catch (ExecutionException e) {
                if (e.getCause() instanceof EoppResourceException eoppResourceException) {
                    exRef.set(eoppResourceException);
                } else {
                    exRef.set(new EoppResourceException(e.getCause()));
                }
                throw new CompletionException(e);
            } catch (Exception e) {
                exRef.set(new EoppResourceException(e));
                throw new CompletionException(exRef.get());
            }
        }, ZIP_EXECUTOR);

        return new FilterInputStream(pis) {
            @Override
            public void close() throws IOException {
                // this will have been called from ZipArchiveOutputStream -> PipedOutputStream, so proceed with closing the PipedInputStream as expected
                super.close();
                // propagate exceptions if needed
                if (exRef.get() != null) throw exRef.get();
            }
        };
    }

    private ZipArchiveEntrySupplier buildZipArchiveEntry(ZipResourceEntry zipResourceEntry) {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipResourceEntry.getPath());
        zipArchiveEntry.setMethod(ZipEntry.DEFLATED);
        zipArchiveEntry.setLastModifiedTime(zipResourceEntry.getLastModified());
        if (zipResourceEntry.getResource() != null) {
            return new ZipArchiveEntrySupplier(zipArchiveEntry, () -> {
                log.trace("Adding resource to ZipArchiveOutputStream: {}", zipArchiveEntry.getName());
                try {
                    return zipResourceEntry.getResource().getInputStream();
                } catch (IOException e) {
                    throw new EoppResourceException(e);
                }
            });
        } else {
            return new ZipArchiveEntrySupplier(zipArchiveEntry, () -> {
                log.trace("Adding directory to ZipArchiveOutputStream: {}", zipArchiveEntry.getName());
                return InputStream.nullInputStream();
            });
        }
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    private record ZipArchiveEntrySupplier(ZipArchiveEntry zipArchiveEntry, InputStreamSupplier inputStreamSupplier) {
    }
}
