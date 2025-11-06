package com.anthropic.claude.sdk.control;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Error control response.
 */
public class ControlErrorResponse {

    @JsonProperty("subtype")
    private final String subtype = "error";

    @JsonProperty("request_id")
    private final String requestId;

    @JsonProperty("error")
    private final String error;

    public ControlErrorResponse(String requestId, String error) {
        this.requestId = requestId;
        this.error = error;
    }

    public String getSubtype() {
        return subtype;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getError() {
        return error;
    }
}
