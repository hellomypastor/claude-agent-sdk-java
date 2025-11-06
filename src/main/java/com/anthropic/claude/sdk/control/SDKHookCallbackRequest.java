package com.anthropic.claude.sdk.control;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;

/**
 * Request to execute a hook callback.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SDKHookCallbackRequest {

    @JsonProperty("subtype")
    private final String subtype = "hook_callback";

    @JsonProperty("callback_id")
    private final String callbackId;

    @JsonProperty("input")
    private final Object input;

    @JsonProperty("tool_use_id")
    @Nullable
    private final String toolUseId;

    public SDKHookCallbackRequest(
            String callbackId,
            Object input,
            @Nullable String toolUseId) {
        this.callbackId = callbackId;
        this.input = input;
        this.toolUseId = toolUseId;
    }

    public String getSubtype() {
        return subtype;
    }

    public String getCallbackId() {
        return callbackId;
    }

    public Object getInput() {
        return input;
    }

    @Nullable
    public String getToolUseId() {
        return toolUseId;
    }
}
