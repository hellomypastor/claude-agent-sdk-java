package com.anthropic.claude.sdk.control;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Successful control response.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ControlResponse {

    @JsonProperty("subtype")
    private final String subtype = "success";

    @JsonProperty("request_id")
    private final String requestId;

    @JsonProperty("response")
    @Nullable
    private final Map<String, Object> response;

    public ControlResponse(String requestId, @Nullable Map<String, Object> response) {
        this.requestId = requestId;
        this.response = response;
    }

    public String getSubtype() {
        return subtype;
    }

    public String getRequestId() {
        return requestId;
    }

    @Nullable
    public Map<String, Object> getResponse() {
        return response;
    }
}
