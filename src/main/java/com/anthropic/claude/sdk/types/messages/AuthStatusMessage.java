package com.anthropic.claude.sdk.types.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Authentication status message.
 */
public final class AuthStatusMessage implements Message {

    private final boolean isAuthenticating;
    private final List<String> output;
    private final String error;
    private final String uuid;
    private final String sessionId;

    public AuthStatusMessage(
            @JsonProperty("is_authenticating") boolean isAuthenticating,
            @JsonProperty("output") List<String> output,
            @JsonProperty("error") String error,
            @JsonProperty("uuid") String uuid,
            @JsonProperty("session_id") String sessionId
    ) {
        this.isAuthenticating = isAuthenticating;
        this.output = output;
        this.error = error;
        this.uuid = uuid;
        this.sessionId = sessionId;
    }

    @JsonProperty("is_authenticating")
    public boolean isAuthenticating() {
        return isAuthenticating;
    }

    @JsonProperty("output")
    public List<String> output() {
        return output;
    }

    @JsonProperty("error")
    public String error() {
        return error;
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
        return "auth_status";
    }
}
