package com.anthropic.claude.sdk.control;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request to interrupt the current operation.
 */
public class SDKControlInterruptRequest {

    @JsonProperty("subtype")
    private final String subtype = "interrupt";

    public SDKControlInterruptRequest() {
    }

    public String getSubtype() {
        return subtype;
    }
}
