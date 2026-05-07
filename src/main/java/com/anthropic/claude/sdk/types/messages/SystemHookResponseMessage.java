package com.anthropic.claude.sdk.types.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * System message containing the response from a hook execution.
 */
public record SystemHookResponseMessage(
        @JsonProperty("hook_name") String hookName,
        @JsonProperty("hook_event") String hookEvent,
        @JsonProperty("stdout") String stdout,
        @JsonProperty("stderr") String stderr,
        @JsonProperty("exit_code") Integer exitCode,
        @JsonProperty("uuid") String uuid,
        @JsonProperty("session_id") String sessionId
) implements Message {

    @Override
    public String getType() {
        return "system";
    }

    public String getSubtype() {
        return "hook_response";
    }
}
