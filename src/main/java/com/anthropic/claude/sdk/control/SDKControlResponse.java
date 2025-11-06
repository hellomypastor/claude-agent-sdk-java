package com.anthropic.claude.sdk.control;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Main SDK control response envelope.
 */
public class SDKControlResponse {

    @JsonProperty("type")
    private final String type = "control_response";

    @JsonProperty("response")
    private final Object response;

    public SDKControlResponse(Object response) {
        this.response = response;
    }

    public String getType() {
        return type;
    }

    public Object getResponse() {
        return response;
    }
}
