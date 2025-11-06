package com.anthropic.claude.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Represents a streaming event from Claude.
 */
public class StreamEvent implements Message {

    private final String uuid;
    private final String sessionId;
    private final String event;
    @Nullable
    private final String parentToolUseId;

    @JsonCreator
    public StreamEvent(
            @JsonProperty("uuid") String uuid,
            @JsonProperty("session_id") String sessionId,
            @JsonProperty("event") String event,
            @JsonProperty("parent_tool_use_id") @Nullable String parentToolUseId) {
        this.uuid = uuid;
        this.sessionId = sessionId;
        this.event = event;
        this.parentToolUseId = parentToolUseId;
    }

    @Override
    public String getRole() {
        return "stream_event";
    }

    public String getUuid() {
        return uuid;
    }

    @JsonProperty("session_id")
    public String getSessionId() {
        return sessionId;
    }

    public String getEvent() {
        return event;
    }

    @Nullable
    @JsonProperty("parent_tool_use_id")
    public String getParentToolUseId() {
        return parentToolUseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StreamEvent that = (StreamEvent) o;
        return Objects.equals(uuid, that.uuid) &&
               Objects.equals(sessionId, that.sessionId) &&
               Objects.equals(event, that.event) &&
               Objects.equals(parentToolUseId, that.parentToolUseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, sessionId, event, parentToolUseId);
    }

    @Override
    public String toString() {
        return "StreamEvent{uuid='" + uuid + "', sessionId='" + sessionId + "', event='" + event + "'}";
    }
}
