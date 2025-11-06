package com.anthropic.claude.sdk.hooks;

/**
 * Types of hook events in the agent loop.
 */
public enum HookEvent {
    PRE_TOOL_USE("PreToolUse"),
    POST_TOOL_USE("PostToolUse"),
    USER_PROMPT_SUBMIT("UserPromptSubmit"),
    STOP("Stop"),
    SUBAGENT_STOP("SubagentStop"),
    PRE_COMPACT("PreCompact");

    private final String value;

    HookEvent(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
