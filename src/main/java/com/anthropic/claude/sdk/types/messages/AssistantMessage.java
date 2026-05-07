package com.anthropic.claude.sdk.types.messages;

import com.anthropic.claude.sdk.types.content.ContentBlock;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Message from Claude (the assistant).
 */
public record AssistantMessage(
        @JsonProperty("message") Object message,
        @JsonProperty("content") List<ContentBlock> content,
        @JsonProperty("model") String model,
        @JsonProperty("parent_tool_use_id") String parentToolUseId,
        @JsonProperty("error") String error,
        @JsonProperty("uuid") String uuid,
        @JsonProperty("session_id") String sessionId
) implements Message {

    @Override
    public String getType() {
        return "assistant";
    }
}
