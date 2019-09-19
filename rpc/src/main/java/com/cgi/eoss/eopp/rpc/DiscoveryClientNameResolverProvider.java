package com.cgi.eoss.eopp.rpc;

import io.grpc.NameResolver;
import io.grpc.NameResolverProvider;
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
        this.discoveryClient = discoveryClient;
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
    public String getDefaultScheme() {
        return SCHEME;
    }

}
