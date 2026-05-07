package com.anthropic.claude.sdk.types.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Authentication status message.
 */
public record AuthStatusMessage(
        @JsonProperty("is_authenticating") boolean isAuthenticating,
        @JsonProperty("output") List<String> output,
        @JsonProperty("error") String error,
        @JsonProperty("uuid") String uuid,
        @JsonProperty("session_id") String sessionId
) implements Message {

    @Override
    public String getType() {
        return "auth_status";
    }
}
