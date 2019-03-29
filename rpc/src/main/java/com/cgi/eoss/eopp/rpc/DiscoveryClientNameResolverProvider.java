package com.cgi.eoss.eopp.rpc;

import io.grpc.NameResolver;
import io.grpc.NameResolverProvider;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import javax.annotation.Nullable;
import java.net.URI;

public class DiscoveryClientNameResolverProvider extends NameResolverProvider {

    private static final String SCHEME = "discovery";

    private final DiscoveryClient discoveryClient;

    public DiscoveryClientNameResolverProvider(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Nullable
    @Override
    public NameResolver newNameResolver(URI targetUri, NameResolver.Helper helper) {
        // We're not quite doing gRPC URIs conventionally, but we expect the URI to be constructed like "discovery://<service-id>"
        String serviceId = targetUri.getHost();
        return new DiscoveryClientNameResolver(discoveryClient, serviceId, helper);
    }

    @Override
    protected boolean isAvailable() {
        return true;
    }

    @Override
    protected int priority() {
        return 8;
    }

    @Override
    public String getDefaultScheme() {
        return SCHEME;
    }

}
