package com.anthropic.claude.sdk.control;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request to set the permission mode.
 */
public class SDKControlSetPermissionModeRequest {

    @JsonProperty("subtype")
    private final String subtype = "set_permission_mode";

    @JsonProperty("mode")
    private final String mode;

    public SDKControlSetPermissionModeRequest(String mode) {
        this.mode = mode;
    }

    public String getSubtype() {
        return subtype;
    }

    public String getMode() {
        return mode;
    }
}
