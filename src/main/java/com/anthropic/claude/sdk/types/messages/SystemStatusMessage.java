package com.anthropic.claude.sdk.types.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * System status message indicating current system state.
 */
public final class SystemStatusMessage implements Message {

    private final String status;
    private final String uuid;
    private final String sessionId;

    public SystemStatusMessage(
            @JsonProperty("status") String status,
            @JsonProperty("uuid") String uuid,
            @JsonProperty("session_id") String sessionId
    ) {
        this.status = status;
        this.uuid = uuid;
        this.sessionId = sessionId;
    }

    @JsonProperty("status")
    public String status() {
        return status;
    }

    @JsonProperty("uuid")
    public String uuid() {
        return uuid;
    }

    @JsonProperty("session_id")
    public String sessionId() {
        return sessionId;
    }

    @Override
    public String getType() {
        return "system";
    }

    public String getSubtype() {
        return "status";
    }
}
