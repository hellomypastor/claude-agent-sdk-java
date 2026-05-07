package com.anthropic.claude.sdk.types.hooks;

/**
 * Supported hook events exposed by the CLI.
 */
public enum HookEvent {
    PRE_TOOL_USE("PreToolUse"),
    POST_TOOL_USE("PostToolUse"),
    POST_TOOL_USE_FAILURE("PostToolUseFailure"),
    NOTIFICATION("Notification"),
    USER_PROMPT_SUBMIT("UserPromptSubmit"),
    SESSION_START("SessionStart"),
    SESSION_END("SessionEnd"),
    STOP("Stop"),
    SUBAGENT_START("SubagentStart"),
    SUBAGENT_STOP("SubagentStop"),
    PRE_COMPACT("PreCompact"),
    PERMISSION_REQUEST("PermissionRequest");

    private final String value;

    HookEvent(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * Look up a HookEvent by its string value.
     *
     * @param value the string value (e.g. "PreToolUse")
     * @return the matching HookEvent
     * @throws IllegalArgumentException if no match is found
     */
    public static HookEvent fromValue(String value) {
        for (HookEvent event : values()) {
            if (event.value.equals(value)) {
                return event;
            }
        }
        throw new IllegalArgumentException("Unknown HookEvent value: " + value);
    }
}
