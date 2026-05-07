package com.anthropic.claude.sdk.types.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Stream event emitted while Claude is still composing a response.
 */
public final class StreamEvent implements Message {

    private final Object event;
    private final String parentToolUseId;
    private final String uuid;
    private final String sessionId;

    public StreamEvent(
            @JsonProperty("event") Object event,
            @JsonProperty("parent_tool_use_id") String parentToolUseId,
            @JsonProperty("uuid") String uuid,
            @JsonProperty("session_id") String sessionId
    ) {
        this.event = event;
        this.parentToolUseId = parentToolUseId;
        this.uuid = uuid;
        this.sessionId = sessionId;
    }

    @JsonProperty("event")
    public Object event() {
        return event;
    }

    @JsonProperty("parent_tool_use_id")
    public String parentToolUseId() {
        return parentToolUseId;
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
        return "stream_event";
    }
}
