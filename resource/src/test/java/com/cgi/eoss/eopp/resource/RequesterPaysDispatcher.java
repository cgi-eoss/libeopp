package com.cgi.eoss.eopp.resource;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.QueueDispatcher;
import okhttp3.mockwebserver.RecordedRequest;
import software.amazon.awssdk.services.s3.model.RequestPayer;

class RequesterPaysDispatcher extends QueueDispatcher {
    @Override
    public MockResponse dispatch(RecordedRequest recordedRequest) throws InterruptedException {
        if (recordedRequest.getHeader("x-amz-request-payer") == null || !recordedRequest.getHeader("x-amz-request-payer").equals(RequestPayer.REQUESTER.toString())) {
            return new MockResponse().setResponseCode(403);
        }
        return super.dispatch(recordedRequest);
    }
}
