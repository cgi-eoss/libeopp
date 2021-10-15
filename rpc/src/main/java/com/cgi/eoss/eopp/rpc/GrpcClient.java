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

import com.cgi.eoss.eopp.util.Lazy;
import io.grpc.ConnectivityState;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.inprocess.InProcessChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

public abstract class GrpcClient {
    private static final Logger log = LoggerFactory.getLogger(GrpcClient.class);

    public static final String IN_PROCESS_URI_PREFIX = "inprocess://";

    private final String serviceUri;
    private final ReentrantLock channelLock = new ReentrantLock();
    // Instantiate a Supplier<ManagedChannel> so that this client can be switched between in-process and standard rpc,
    // and do it lazily so this class can be further configured before invocation
    private final Supplier<Supplier<ManagedChannel>> channelSupplierSupplier = Lazy.lazily(this::buildChannelSupplier);
    // The gRPC channel managed by this client. Recreated if necessary after connectivity failures
    private final AtomicReference<ManagedChannel> channel = new AtomicReference<>();

    /**
     * <p>Construct a gRPC client channel manager for the given service URI.</p>
     *
     * @param serviceUri The gRPC service URI. In addition to grpc-java supported URIs (including plain DNS hostnames)
     *                   this service supports "inprocess://servicename" to manage in-process gRPC channels.
     */
    protected GrpcClient(String serviceUri) {
        this.serviceUri = serviceUri;
    }

    /**
     * @return A gRPC channel to the configured service. Will be created if no channel exists, and will be reconnected
     * if the channel is in a non-usable state.
     */
    protected ManagedChannel getChannel() {
        // if necessary, update the local reference and return it, otherwise re-establish the channel
        lockChannel();
        try {
            return channel.updateAndGet(current -> Optional.ofNullable(current)
                    .filter(c -> !EnumSet.of(ConnectivityState.TRANSIENT_FAILURE, ConnectivityState.SHUTDOWN).contains(c.getState(true))) // if the channel is not in a 'bad' state, return it...
                    .orElseGet(this::establishNewChannel)); // ... otherwise create a new channel
        } finally {
            unlockChannel();
        }
    }

    /**
     * @return A supplier to build a new {@link ManagedChannel}, by default based on the configured {@link #serviceUri}.
     */
    protected Supplier<ManagedChannel> buildChannelSupplier() {
        if (serviceUri.startsWith(IN_PROCESS_URI_PREFIX)) {
            return () -> configureInProcessChannelBuilder(InProcessChannelBuilder.forName(serviceUri.substring(12))).build();
        } else {
            return () -> configureChannelBuilder(ManagedChannelBuilder.forTarget(serviceUri)).build();
        }
    }

    /**
     * <p>Extension point to further configure the {@link ManagedChannelBuilder} for an in-process gRPC connection.</p>
     */
    protected <T extends ManagedChannelBuilder<T>> ManagedChannelBuilder<T> configureInProcessChannelBuilder(ManagedChannelBuilder<T> managedChannelBuilder) {
        return managedChannelBuilder;
    }

    /**
     * <p>Extension point to further configure the {@link ManagedChannelBuilder} for a gRPC connection.</p>
     */
    protected <T extends ManagedChannelBuilder<T>> ManagedChannelBuilder<T> configureChannelBuilder(ManagedChannelBuilder<T> managedChannelBuilder) {
        return managedChannelBuilder
                .usePlaintext(); // TODO TLS
    }

    /**
     * <p>Lock control flow through the {@link #getChannel()} method. For example, this can be used in subclasses to
     * delay channel creation until some external initialisation is complete.</p>
     */
    protected final void lockChannel() {
        channelLock.lock();
    }

    /**
     * <p>Unlock control flow through the {@link #getChannel()} method. For example, this can be used in subclasses to
     * delay channel creation until some external initialisation is complete.</p>
     */
    protected final void unlockChannel() {
        channelLock.unlock();
    }

    private ManagedChannel establishNewChannel() {
        return channel.updateAndGet(current -> {
            Optional.ofNullable(current).ifPresent(GrpcClient.this::safeShutdownChannel);
            ManagedChannel newChannel = channelSupplierSupplier.get().get();
            log.info("Established new channel: {}", newChannel);
            return newChannel;
        });
    }

    private void safeShutdownChannel(ManagedChannel managedChannel) {
        if (!managedChannel.isShutdown()) {
            log.info("Shutting down channel: {}", managedChannel);
            managedChannel.shutdown();
        }
    }

}
