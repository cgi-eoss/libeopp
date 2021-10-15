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

package com.cgi.eoss.eopp.rpc;

import io.grpc.NameResolver;
import io.grpc.NameResolverProvider;
import io.grpc.NameResolverRegistry;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import javax.annotation.Nullable;
import java.net.URI;

/**
 * <p>Provider for {@link DiscoveryClientNameResolver} instances.</p>
 */
public class DiscoveryClientNameResolverProvider extends NameResolverProvider {

    private static final String SCHEME = "discovery";
    private static final int PRIORITY = 8;

    private final DiscoveryClient discoveryClient;

    public DiscoveryClientNameResolverProvider(DiscoveryClient discoveryClient) {
        this(discoveryClient, true);
    }

    public DiscoveryClientNameResolverProvider(DiscoveryClient discoveryClient, boolean registerSelf) {
        this.discoveryClient = discoveryClient;
        if (registerSelf) {
            NameResolverRegistry.getDefaultRegistry().register(this);
        }
    }

    @Nullable
    @Override
    public NameResolver newNameResolver(URI targetUri, NameResolver.Args args) {
        // We're not quite doing gRPC URIs conventionally, but we expect the URI to be constructed like "discovery://<service-id>"
        String serviceId = targetUri.getHost();
        return new DiscoveryClientNameResolver(discoveryClient, serviceId);
    }

    @Override
    protected boolean isAvailable() {
        return true;
    }

    @Override
    protected int priority() {
        return PRIORITY;
    }

    @Override
    protected String getScheme() {
        return SCHEME;
    }

    @Override
    public String getDefaultScheme() {
        return SCHEME;
    }

}
