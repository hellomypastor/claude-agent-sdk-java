package com.anthropic.claude.sdk.types.messages;

import com.anthropic.claude.sdk.types.content.ContentBlock;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Message from the user.
 */
@Data
@AllArgsConstructor
public final class UserMessage implements Message {
    @JsonProperty("content")
    private final List<ContentBlock> content;

    @JsonProperty("parent_tool_use_id")
    private final String parentToolUseId;

    @Override
    public String getType() {
        return "user";
    }
}
