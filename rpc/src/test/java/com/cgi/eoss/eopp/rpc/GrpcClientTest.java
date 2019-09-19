package com.cgi.eoss.eopp.rpc;

import io.grpc.examples.helloworld.GreeterGrpc;
import io.grpc.examples.helloworld.HelloReply;
import io.grpc.examples.helloworld.HelloRequest;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.stubbing.Answer;

import java.io.IOException;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

@RunWith(JUnit4.class)
public class GrpcClientTest {

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
    public void testInProcessChannels() throws IOException {
        String serverName = InProcessServerBuilder.generateName();
        grpcCleanup.register(InProcessServerBuilder.forName(serverName).directExecutor().addService(serviceImpl).build().start());

        HelloWorldClient helloWorldClient = new HelloWorldClient("inprocess://" + serverName, grpcCleanup);

        GreeterGrpc.GreeterBlockingStub greeterBlockingStub = helloWorldClient.getBlockingStub();
        HelloReply reply = greeterBlockingStub.sayHello(HelloRequest.newBuilder().setName("test name").build());
        assertThat(reply.getMessage()).isEqualTo("Hello test name");
    }

}