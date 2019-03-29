package com.cgi.eoss.eopp.rpc;

import com.cgi.eoss.eopp.util.Lazy;
import io.grpc.ConnectivityState;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.NameResolver;
import io.grpc.NameResolverProvider;
import io.grpc.inprocess.InProcessChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

abstract class GrpcClient {
    private static final Logger log = LoggerFactory.getLogger(GrpcClient.class);

    private final String serviceUri;
    private final ReentrantLock channelLock = new ReentrantLock();
    private final NameResolver.Factory nameResolverFactory;
    // Instantiate a Supplier<ManagedChannel> so that this client can be switched between in-process and standard rpc,
    // and do it lazily so its creation can access gRpcServerProperties
    private final Supplier<Supplier<ManagedChannel>> channelSupplierSupplier = Lazy.lazily(this::buildChannelSupplier);
    private ManagedChannel channel;

    GrpcClient(String serviceUri) {
        this.serviceUri = serviceUri;
        this.nameResolverFactory = NameResolverProvider.asFactory();
    }

    GrpcClient(String serviceUri, DiscoveryClientNameResolverProvider discoveryClientNameResolverProvider) {
        this.serviceUri = serviceUri;
        this.nameResolverFactory = discoveryClientNameResolverProvider;
    }

    private Supplier<ManagedChannel> buildChannelSupplier() {
        if (serviceUri.startsWith("inprocess://")) {
            return () -> InProcessChannelBuilder.forName(serviceUri.substring(12)).build();
        } else {
            return () -> ManagedChannelBuilder.forTarget(serviceUri)
                    .nameResolverFactory(nameResolverFactory)
                    .usePlaintext() // TODO TLS
                    .build();
        }
    }

    ManagedChannel getChannel() {
        // if necessary, update the local reference and return it, otherwise re-establish the channel
        channelLock.lock();
        try {
            return channel = Optional.ofNullable(channel)
                    .filter(c -> !EnumSet.of(ConnectivityState.TRANSIENT_FAILURE, ConnectivityState.SHUTDOWN).contains(c.getState(true))) // if the channel is not in a 'bad' state, return it...
                    .orElseGet(this::establishNewChannel); // ... otherwise create a new channel
        } finally {
            channelLock.unlock();
        }
    }

    private ManagedChannel establishNewChannel() {
        Optional.ofNullable(channel).ifPresent(this::safeShutdownChannel);
        ManagedChannel newChannel = channelSupplierSupplier.get().get();
        log.info("Established new channel: {}", newChannel);
        return newChannel;
    }

    private void safeShutdownChannel(ManagedChannel managedChannel) {
        if (!managedChannel.isShutdown()) {
            log.info("Shutting down channel: {}", managedChannel);
            managedChannel.shutdown();
        }
    }

}
