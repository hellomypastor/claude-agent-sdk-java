package com.anthropic.claude.sdk.types.messages;

import com.anthropic.claude.sdk.types.content.ContentBlock;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Message from the user.
 */
public record UserMessage(
        @JsonProperty("content") List<ContentBlock> content,
        @JsonProperty("parent_tool_use_id") String parentToolUseId,
        @JsonProperty("is_synthetic") boolean isSynthetic,
        @JsonProperty("tool_use_result") Object toolUseResult,
        @JsonProperty("uuid") String uuid,
        @JsonProperty("session_id") String sessionId,
        @JsonProperty("is_replay") Boolean isReplay
) implements Message {

    @Override
    public String getType() {
        return "user";
    }
}
