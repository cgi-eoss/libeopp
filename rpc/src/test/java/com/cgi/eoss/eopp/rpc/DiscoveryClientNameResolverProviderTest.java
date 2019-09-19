package com.cgi.eoss.eopp.rpc;

import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.examples.helloworld.GreeterGrpc;
import io.grpc.examples.helloworld.HelloReply;
import io.grpc.examples.helloworld.HelloRequest;
import io.grpc.netty.NettyServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.stubbing.Answer;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Collections;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class DiscoveryClientNameResolverProviderTest {

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    private final GreeterGrpc.GreeterImplBase serviceImpl = mock(GreeterGrpc.GreeterImplBase.class);

    @Before
    public void setUp() {
        doAnswer((Answer<Void>) invocation -> {
            HelloRequest request = invocation.getArgument(0);
            StreamObserver<HelloReply> streamObserver = invocation.getArgument(1);
            streamObserver.onNext(HelloReply.newBuilder().setMessage("Hello " + request.getName()).build());
            streamObserver.onCompleted();
            return null;
        }).when(serviceImpl).sayHello(any(), any());
    }

    @Test
    public void testRealServerAndReconnection() throws IOException {
        Server server = NettyServerBuilder.forPort(getRandomPort()).addService(serviceImpl).build().start();
        grpcCleanup.register(server);

        // Set up a mock spring-cloud discovery response matching the local Netty gRPC server instance
        DiscoveryClient discoveryClient = mock(DiscoveryClient.class);
        DiscoveryClientNameResolverProvider discoveryClientNameResolverProvider = new DiscoveryClientNameResolverProvider(discoveryClient);
        ServiceInstance serviceInstance = mock(ServiceInstance.class);
        when(serviceInstance.getHost()).thenReturn("localhost");
        when(serviceInstance.getPort()).thenReturn(server.getPort());
        when(discoveryClient.getInstances("localhost")).thenReturn(Collections.singletonList(serviceInstance));

        assertThat(discoveryClientNameResolverProvider.getDefaultScheme()).isEqualTo("discovery");
        assertThat(discoveryClientNameResolverProvider.isAvailable()).isTrue();
        assertThat(discoveryClientNameResolverProvider.priority()).isEqualTo(8);

        HelloWorldClient helloWorldClient = new HelloWorldClient("discovery://localhost:" + server.getPort(), discoveryClientNameResolverProvider, grpcCleanup);

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