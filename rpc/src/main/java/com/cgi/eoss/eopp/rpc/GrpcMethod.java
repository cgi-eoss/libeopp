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

import com.google.common.base.MoreObjects;
import com.google.protobuf.Message;
import io.grpc.MethodDescriptor;
import io.grpc.stub.AbstractStub;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * <p>A holder for a method call to a gRPC service. For example, a prepared call for future execution.</p>
 *
 * @param <S> Type of the gRPC service stub.
 * @param <P> Type of the parameter of the method, i.e. request message type.
 * @param <R> Type of the response of the method, i.e. response message type.
 */
public final class GrpcMethod<S extends AbstractStub<S>, P, R> {

    private final AbstractStub<S> stub;
    private final MethodDescriptor<P, R> methodDescriptor;
    private final P request;

    /**
     * <p>Create a new method reference with all necessary information for a future gRPC service call.</p>
     */
    public GrpcMethod(AbstractStub<S> stub, MethodDescriptor<P, R> methodDescriptor, P request) {
        this.stub = stub;
        this.methodDescriptor = methodDescriptor;
        this.request = request;
    }

    /**
     * @return The gRPC service stub on which to execute the given method.
     */
    public AbstractStub<S> getStub() {
        return this.stub;
    }

    /**
     * @return The gRPC method to be executed.
     */
    public MethodDescriptor<P, R> getMethodDescriptor() {
        return this.methodDescriptor;
    }

    /**
     * @return The gRPC request message to send to the specified method.
     */
    public P getRequest() {
        return this.request;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("stub", stub)
                .add("methodDescriptor", methodDescriptor)
                .add("request", request)
                .toString();
    }

    /**
     * @return A URI representing this gRPC method call.
     */
    public URI toURI() {
        StringBuilder uri = new StringBuilder("grpc://")
                .append(stub.getChannel().authority()).append("/")
                .append(methodDescriptor.getFullMethodName())
                .append("?");
        ((Message) request).getAllFields()
                .forEach((key, value) ->
                        uri.append(key.getName())
                                .append("=")
                                .append(encodeParameterValue(value.toString())));
        try {
            return new URI(uri.toString());
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }

    private static String encodeParameterValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            // If StandardCharsets.UTF_8 is unsupported, we have a problem
            throw new IllegalStateException(e);
        }
    }

}