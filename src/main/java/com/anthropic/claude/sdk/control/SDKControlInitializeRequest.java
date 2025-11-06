package com.anthropic.claude.sdk.control;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Request to initialize the SDK with hooks configuration.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SDKControlInitializeRequest {

    @JsonProperty("subtype")
    private final String subtype = "initialize";

    @JsonProperty("hooks")
    @Nullable
    private final Map<String, Object> hooks;

    public SDKControlInitializeRequest(@Nullable Map<String, Object> hooks) {
        this.hooks = hooks;
    }

    public String getSubtype() {
        return subtype;
    }

    @Nullable
    public Map<String, Object> getHooks() {
        return hooks;
    }
}
