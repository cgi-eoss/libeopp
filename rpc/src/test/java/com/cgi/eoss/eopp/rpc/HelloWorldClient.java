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
