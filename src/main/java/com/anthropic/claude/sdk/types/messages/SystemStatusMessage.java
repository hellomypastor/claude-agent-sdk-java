package com.anthropic.claude.sdk.types.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * System status message indicating current system state.
 */
public record SystemStatusMessage(
        @JsonProperty("status") String status,
        @JsonProperty("uuid") String uuid,
        @JsonProperty("session_id") String sessionId
) implements Message {

    @Override
    public String getType() {
        return "system";
    }

    public String getSubtype() {
        return "status";
    }
}
