package com.anthropic.claude.sdk.types.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Progress update for a tool execution in progress.
 */
public final class ToolProgressMessage implements Message {

    private final String toolUseId;
    private final String toolName;
    private final String parentToolUseId;
    private final double elapsedTimeSeconds;
    private final String uuid;
    private final String sessionId;

    public ToolProgressMessage(
            @JsonProperty("tool_use_id") String toolUseId,
            @JsonProperty("tool_name") String toolName,
            @JsonProperty("parent_tool_use_id") String parentToolUseId,
            @JsonProperty("elapsed_time_seconds") double elapsedTimeSeconds,
            @JsonProperty("uuid") String uuid,
            @JsonProperty("session_id") String sessionId
    ) {
        this.toolUseId = toolUseId;
        this.toolName = toolName;
        this.parentToolUseId = parentToolUseId;
        this.elapsedTimeSeconds = elapsedTimeSeconds;
        this.uuid = uuid;
        this.sessionId = sessionId;
    }

    @JsonProperty("tool_use_id")
    public String toolUseId() {
        return toolUseId;
    }

    @JsonProperty("tool_name")
    public String toolName() {
        return toolName;
    }

    @JsonProperty("parent_tool_use_id")
    public String parentToolUseId() {
        return parentToolUseId;
    }

    @JsonProperty("elapsed_time_seconds")
    public double elapsedTimeSeconds() {
        return elapsedTimeSeconds;
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
        return "tool_progress";
    }
}
