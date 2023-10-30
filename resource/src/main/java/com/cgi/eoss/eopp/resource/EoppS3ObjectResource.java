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

import org.springframework.core.io.Resource;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

import java.io.InputStream;
import java.util.Optional;

/**
 * <p>{@link EoppResource} implementation representing data in an S3 (or S3-compatible) bucket.</p>
 * <p>If the libeopp application is working with its own S3 bucket for local storage, this class may be extended to make
 * {@link #isCacheable()} return <code>false</code> for local resources.</p>
 *
 * @see EoppS3ObjectAsyncResource
 */
public class EoppS3ObjectResource extends BaseS3ObjectResource implements EoppResource {

    private final S3Client s3Client;

    public EoppS3ObjectResource(S3Client s3Client, String bucket, String key) {
        this(s3Client, bucket, key, false);
    }

    public EoppS3ObjectResource(S3Client s3Client, String bucket, String key, boolean requesterPays) {
        super(bucket, key, requesterPays);
        this.s3Client = s3Client;
    }

    @Override
    public String getDescription() {
        return "EoppS3ObjectResource [" + getURI() + "]";
    }

    @Override
    protected Resource doCreateRelative(String bucket, String key) {
        return new EoppS3ObjectResource(s3Client, bucket, key);
    }

    @Override
    protected InputStream doGetInputStream(GetObjectRequest request) {
        try {
            return s3Client.getObject(request);
        } catch (Exception e) {
            throw new EoppResourceException("Failed to complete S3 resource GET request", e);
        }
    }

    @Override
    protected Optional<HeadObjectResponse> headObject(HeadObjectRequest headObjectRequest) {
        try {
            return Optional.of(s3Client.headObject(headObjectRequest));
        } catch (NoSuchKeyException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new EoppResourceException("Failed to complete S3 resource HEAD request", e);
        }
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
