package com.anthropic.claude.sdk.types.messages;

import com.anthropic.claude.sdk.types.content.ContentBlock;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Message from the user.
 */
public final class UserMessage implements Message {

    private final List<ContentBlock> content;
    private final String parentToolUseId;
    private final boolean isSynthetic;
    private final Object toolUseResult;
    private final String uuid;
    private final String sessionId;
    private final Boolean isReplay;

    public UserMessage(
            @JsonProperty("content") List<ContentBlock> content,
            @JsonProperty("parent_tool_use_id") String parentToolUseId,
            @JsonProperty("is_synthetic") boolean isSynthetic,
            @JsonProperty("tool_use_result") Object toolUseResult,
            @JsonProperty("uuid") String uuid,
            @JsonProperty("session_id") String sessionId,
            @JsonProperty("is_replay") Boolean isReplay
    ) {
        this.content = content;
        this.parentToolUseId = parentToolUseId;
        this.isSynthetic = isSynthetic;
        this.toolUseResult = toolUseResult;
        this.uuid = uuid;
        this.sessionId = sessionId;
        this.isReplay = isReplay;
    }

    @JsonProperty("content")
    public List<ContentBlock> content() {
        return content;
    }

    @JsonProperty("parent_tool_use_id")
    public String parentToolUseId() {
        return parentToolUseId;
    }

    @JsonProperty("is_synthetic")
    public boolean isSynthetic() {
        return isSynthetic;
    }

    @JsonProperty("tool_use_result")
    public Object toolUseResult() {
        return toolUseResult;
    }

    @JsonProperty("uuid")
    public String uuid() {
        return uuid;
    }

    @JsonProperty("session_id")
    public String sessionId() {
        return sessionId;
    }

    @JsonProperty("is_replay")
    public Boolean isReplay() {
        return isReplay;
    }

    @Override
    public String getType() {
        return "user";
    }
}
