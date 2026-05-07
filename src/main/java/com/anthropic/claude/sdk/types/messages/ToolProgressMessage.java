package com.anthropic.claude.sdk.types.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Progress update for a tool execution in progress.
 */
public record ToolProgressMessage(
        @JsonProperty("tool_use_id") String toolUseId,
        @JsonProperty("tool_name") String toolName,
        @JsonProperty("parent_tool_use_id") String parentToolUseId,
        @JsonProperty("elapsed_time_seconds") double elapsedTimeSeconds,
        @JsonProperty("uuid") String uuid,
        @JsonProperty("session_id") String sessionId
) implements Message {

    @Override
    public String getType() {
        return "tool_progress";
    }
}
