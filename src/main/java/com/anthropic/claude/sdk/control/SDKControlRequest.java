package com.anthropic.claude.sdk.control;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Main SDK control request envelope.
 */
public class SDKControlRequest {

    @JsonProperty("type")
    private final String type = "control_request";

    @JsonProperty("request_id")
    private final String requestId;

    @JsonProperty("request")
    private final Object request;

    public SDKControlRequest(String requestId, Object request) {
        this.requestId = requestId;
        this.request = request;
    }

    public String getType() {
        return type;
    }

    public String getRequestId() {
        return requestId;
    }

    public Object getRequest() {
        return request;
    }
}
