package com.anthropic.claude.sdk.types.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Stream event emitted while Claude is still composing a response.
 */
public record StreamEvent(
        @JsonProperty("event") Object event,
        @JsonProperty("parent_tool_use_id") String parentToolUseId,
        @JsonProperty("uuid") String uuid,
        @JsonProperty("session_id") String sessionId
) implements Message {

    @Override
    public String getType() {
        return "stream_event";
    }
}
