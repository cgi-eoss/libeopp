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

import io.grpc.Attributes;
import io.grpc.EquivalentAddressGroup;
import io.grpc.NameResolver;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.net.InetSocketAddress;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * <p>A {@link NameResolver} backed by a {@link DiscoveryClient}. Locates the gRPC service port via the instance
 * metadata map.</p>
 */
public class DiscoveryClientNameResolver extends NameResolver {
    private static final String GRPC_PORT_METADATA_KEY = "port.grpc";

    private final DiscoveryClient discoveryClient;
    private final String serviceId;

    private Listener listener;

    public DiscoveryClientNameResolver(DiscoveryClient discoveryClient, String serviceId) {
        this.discoveryClient = discoveryClient;
        this.serviceId = serviceId;
    }

    @Override
    public String getServiceAuthority() {
        return serviceId;
    }

    @Override
    public void start(Listener listener) {
        this.listener = listener;
        refresh();
    }

    @Override
    public void refresh() {
        // Convert all ServiceInstances into gRPC's address type
        List<EquivalentAddressGroup> servers = discoveryClient.getInstances(serviceId).stream()
                .map(serviceInstance -> new InetSocketAddress(serviceInstance.getHost(), getGrpcPort(serviceInstance)))
                .map(EquivalentAddressGroup::new) // TODO
                .toList();
        // TODO Extract service config attributes?
        this.listener.onAddresses(servers, Attributes.EMPTY);
    }

    @Override
    public void shutdown() {
        // nothing to do
    }

    private int getGrpcPort(ServiceInstance serviceInstance) {
        return Integer.parseInt(serviceInstance.getMetadata().getOrDefault(GRPC_PORT_METADATA_KEY, Integer.toString(serviceInstance.getPort())));
    }

}
