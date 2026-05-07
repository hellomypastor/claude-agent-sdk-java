package com.anthropic.claude.sdk.types.messages;

import com.anthropic.claude.sdk.types.content.ContentBlock;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Message from Claude (the assistant).
 */
public final class AssistantMessage implements Message {

    private final Object message;
    private final List<ContentBlock> content;
    private final String model;
    private final String parentToolUseId;
    private final String error;
    private final String uuid;
    private final String sessionId;

    public AssistantMessage(
            @JsonProperty("message") Object message,
            @JsonProperty("content") List<ContentBlock> content,
            @JsonProperty("model") String model,
            @JsonProperty("parent_tool_use_id") String parentToolUseId,
            @JsonProperty("error") String error,
            @JsonProperty("uuid") String uuid,
            @JsonProperty("session_id") String sessionId
    ) {
        this.message = message;
        this.content = content;
        this.model = model;
        this.parentToolUseId = parentToolUseId;
        this.error = error;
        this.uuid = uuid;
        this.sessionId = sessionId;
    }

    @JsonProperty("message")
    public Object message() {
        return message;
    }

    @JsonProperty("content")
    public List<ContentBlock> content() {
        return content;
    }

    @JsonProperty("model")
    public String model() {
        return model;
    }

    @JsonProperty("parent_tool_use_id")
    public String parentToolUseId() {
        return parentToolUseId;
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
        return "assistant";
    }
}
