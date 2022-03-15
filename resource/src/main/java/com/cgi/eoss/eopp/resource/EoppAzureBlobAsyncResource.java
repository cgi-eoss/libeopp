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

import com.azure.storage.blob.BlobServiceAsyncClient;
import com.azure.storage.blob.models.BlobErrorCode;
import com.azure.storage.blob.models.BlobProperties;
import com.azure.storage.blob.models.BlobStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.URI;
import java.util.Optional;

/**
 * <p>{@link EoppResource} implementation representing data in an Azure Blob Storage container.</p>
 * <p>If the libeopp application is working with its own Azure Blob Storage container for local storage,
 * this class may be extended to make {@link #isCacheable()} return <code>false</code> for local
 * resources.</p>
 */
public class EoppAzureBlobAsyncResource extends BaseAzureBlobResource {
    private static final Logger log = LoggerFactory.getLogger(EoppAzureBlobAsyncResource.class);

    private final BlobServiceAsyncClient blobServiceAsyncClient;

    protected EoppAzureBlobAsyncResource(BlobServiceAsyncClient blobServiceAsyncClient, String container, String name) {
        super(container, name);
        this.blobServiceAsyncClient = blobServiceAsyncClient;
    }

    @Override
    protected URI doGetURI(String container, String name) {
        String accountUrl = blobServiceAsyncClient.getAccountUrl();
        return URI.create((accountUrl.endsWith("/") ? accountUrl : accountUrl + "/") + container + "/" + name);
    }

    @Override
    protected Resource doCreateRelative(String bucket, String key) {
        return new EoppAzureBlobAsyncResource(blobServiceAsyncClient, bucket, key);
    }

    @Override
    protected InputStream doGetInputStream(String container, String name) {
        try {
            PipedInputStream pis = new PipedInputStream();
            PipedOutputStream pipedOutputStream = new PipedOutputStream(pis);

            DataBufferUtils.write(getDataBufferFlux(container, name), pipedOutputStream)
                    .doFinally(type -> {
                        try {
                            pipedOutputStream.close();
                        } catch (IOException e) {
                            throw Exceptions.propagate(e);
                        }
                    })
                    .subscribeOn(Schedulers.boundedElastic())
                    .subscribe(DataBufferUtils.releaseConsumer());

            return pis;
        } catch (Exception e) {
            throw new EoppResourceException("Failed to open InputStream for Azure Blob", e);
        }
    }

    private Flux<DataBuffer> getDataBufferFlux(String container, String name) {
        return blobServiceAsyncClient
                .getBlobContainerAsyncClient(container)
                .getBlobAsyncClient(name)
                .downloadStream()
                .map(DefaultDataBufferFactory.sharedInstance::wrap);
    }

    @Override
    protected Optional<BlobProperties> getBlobProperties(String container, String name) {
        try {
            return blobServiceAsyncClient.getBlobContainerAsyncClient(container).getBlobAsyncClient(name).getProperties()
                    .blockOptional();
        } catch (Exception e) {
            if (e instanceof BlobStorageException && ((BlobStorageException) e).getErrorCode().equals(BlobErrorCode.BLOB_NOT_FOUND)) {
                return Optional.empty();
            }
            throw new EoppResourceException("Failed to get Azure Blob properties", e);
        }
    }
}
