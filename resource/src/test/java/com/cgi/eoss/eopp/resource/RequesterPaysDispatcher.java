package com.cgi.eoss.eopp.resource;

import mockwebserver3.MockResponse;
import mockwebserver3.QueueDispatcher;
import mockwebserver3.RecordedRequest;
import software.amazon.awssdk.services.s3.model.RequestPayer;

import java.util.Objects;

class RequesterPaysDispatcher extends QueueDispatcher {
    @Override
    public MockResponse dispatch(RecordedRequest recordedRequest) throws InterruptedException {
        if (!Objects.equals(recordedRequest.getHeaders().get("x-amz-request-payer"), RequestPayer.REQUESTER.toString())) {
            return new MockResponse.Builder().code(403).build();
        }
        return super.dispatch(recordedRequest);
    }
}
