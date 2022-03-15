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

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobErrorCode;
import com.azure.storage.blob.models.BlobProperties;
import com.azure.storage.blob.models.BlobStorageException;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.net.URI;
import java.util.Optional;

/**
 * <p>{@link EoppResource} implementation representing data in an Azure Blob Storage container.</p>
 * <p>If the libeopp application is working with its own Azure Blob Storage container for local storage,
 * this class may be extended to make {@link #isCacheable()} return <code>false</code> for local
 * resources.</p>
 */
public class EoppAzureBlobResource extends BaseAzureBlobResource {
    private final BlobServiceClient blobServiceClient;

    protected EoppAzureBlobResource(BlobServiceClient blobServiceClient, String container, String name) {
        super(container, name);
        this.blobServiceClient = blobServiceClient;
    }

    @Override
    protected URI doGetURI(String container, String name) {
        String accountUrl = blobServiceClient.getAccountUrl();
        return URI.create((accountUrl.endsWith("/") ? accountUrl : accountUrl + "/") + container + "/" + name);
    }

    @Override
    protected Resource doCreateRelative(String bucket, String key) {
        return new EoppAzureBlobResource(blobServiceClient, bucket, key);
    }

    @Override
    protected InputStream doGetInputStream(String container, String name) {
        try {
            return blobServiceClient
                    .getBlobContainerClient(container)
                    .getBlobClient(name)
                    .openInputStream();
        } catch (Exception e) {
            throw new EoppResourceException("Failed to open InputStream for Azure Blob", e);
        }
    }

    @Override
    protected Optional<BlobProperties> getBlobProperties(String container, String name) {
        try {
            return Optional.of(blobServiceClient.getBlobContainerClient(container).getBlobClient(name).getProperties());
        } catch (Exception e) {
            if (e instanceof BlobStorageException && ((BlobStorageException) e).getErrorCode().equals(BlobErrorCode.BLOB_NOT_FOUND)) {
                return Optional.empty();
            }
            throw new EoppResourceException("Failed to get Azure Blob properties", e);
        }
    }
}
