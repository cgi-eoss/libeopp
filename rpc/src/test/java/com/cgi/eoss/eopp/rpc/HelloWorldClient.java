package com.cgi.eoss.eopp.rpc;

import io.grpc.ManagedChannel;
import io.grpc.NameResolver;
import io.grpc.examples.helloworld.GreeterGrpc;
import io.grpc.testing.GrpcCleanupRule;

class HelloWorldClient extends GrpcClient {
    private final GrpcCleanupRule grpcCleanup;
    ManagedChannel currentChannel;

    HelloWorldClient(String serviceUri, GrpcCleanupRule grpcCleanup) {
        super(serviceUri);
        this.grpcCleanup = grpcCleanup;
    }

    HelloWorldClient(String serviceUri, NameResolver.Factory nameResolverFactory, GrpcCleanupRule grpcCleanup) {
        super(serviceUri, nameResolverFactory);
        this.grpcCleanup = grpcCleanup;
    }

    GreeterGrpc.GreeterBlockingStub getBlockingStub() {
        currentChannel = grpcCleanup.register(getChannel());
        return GreeterGrpc.newBlockingStub(currentChannel);
    }
}
