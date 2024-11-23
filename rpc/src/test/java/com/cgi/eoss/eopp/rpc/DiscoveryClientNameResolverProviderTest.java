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

import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.examples.helloworld.GreeterGrpc;
import io.grpc.examples.helloworld.HelloReply;
import io.grpc.examples.helloworld.HelloRequest;
import io.grpc.netty.NettyServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClient;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryProperties;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static java.util.stream.Collectors.toList;

@RunWith(JUnit4.class)
public class DiscoveryClientNameResolverProviderTest {

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    private final GreeterGrpc.GreeterImplBase serviceImpl = new GreeterGrpc.GreeterImplBase() {
        @Override
        public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
            responseObserver.onNext(HelloReply.newBuilder().setMessage("Hello " + request.getName()).build());
            responseObserver.onCompleted();
        }
    };

    @Test
    public void testRealServerAndReconnection() throws IOException {
        Server server = NettyServerBuilder.forAddress(new InetSocketAddress("localhost", getRandomPort()))
                .addService(serviceImpl).build().start();
        grpcCleanup.register(server);

        // Set up a spring-cloud discovery environment matching the local gRPC server instance
        SimpleDiscoveryProperties simpleDiscoveryProperties = new SimpleDiscoveryProperties();
        simpleDiscoveryProperties.setInstances(Map.of("helloService", server.getListenSockets().stream()
                .map(listenSocket -> new DefaultServiceInstance(null, "helloService", ((InetSocketAddress) listenSocket).getHostString(), ((InetSocketAddress) listenSocket).getPort(), false))
                .toList()));
        DiscoveryClient discoveryClient = new SimpleDiscoveryClient(simpleDiscoveryProperties);

        DiscoveryClientNameResolverProvider discoveryClientNameResolverProvider = new DiscoveryClientNameResolverProvider(discoveryClient);

        assertThat(discoveryClientNameResolverProvider.getDefaultScheme()).isEqualTo("discovery");
        assertThat(discoveryClientNameResolverProvider.isAvailable()).isTrue();
        assertThat(discoveryClientNameResolverProvider.priority()).isEqualTo(8);

        HelloWorldClient helloWorldClient = new HelloWorldClient("discovery://helloService", grpcCleanup);

        GreeterGrpc.GreeterBlockingStub greeterBlockingStub = helloWorldClient.getBlockingStub();
        HelloReply reply = greeterBlockingStub.sayHello(HelloRequest.newBuilder().setName("test name").build());
        assertThat(reply.getMessage()).isEqualTo("Hello test name");

        // Test reconnection by shutting down the channel, then using the client again
        ManagedChannel firstChannel = helloWorldClient.currentChannel;
        firstChannel.shutdownNow(); // TODO Test TRANSIENT_FAILURE state?

        greeterBlockingStub = helloWorldClient.getBlockingStub();
        reply = greeterBlockingStub.sayHello(HelloRequest.newBuilder().setName("test name").build());
        assertThat(reply.getMessage()).isEqualTo("Hello test name");

        assertThat(helloWorldClient.currentChannel).isNotSameInstanceAs(firstChannel);
    }

    private int getRandomPort() throws IOException {
        try (ServerSocket s = new ServerSocket(0)) {
            return s.getLocalPort();
        }
    }

}