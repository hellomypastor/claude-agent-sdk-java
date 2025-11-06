package com.anthropic.claude.sdk.types.messages;

import com.anthropic.claude.sdk.types.content.ContentBlock;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Message from Claude (the assistant).
 */
@Data
@AllArgsConstructor
public final class AssistantMessage implements Message {
    @JsonProperty("content")
    private final List<ContentBlock> content;

    @JsonProperty("model")
    private final String model;

    @JsonProperty("parent_tool_use_id")
    private final String parentToolUseId;

    @Override
    public String getType() {
        return "assistant";
    }
}
