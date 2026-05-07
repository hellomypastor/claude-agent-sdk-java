package com.anthropic.claude.sdk.types.hooks;

/**
 * Sealed interface for all hook input types.
 * Each hook event has a corresponding input type that carries event-specific data.
 */
public sealed interface HookInput
        permits PreToolUseHookInput, PostToolUseHookInput, PostToolUseFailureHookInput,
                NotificationHookInput, UserPromptSubmitHookInput, SessionStartHookInput,
                SessionEndHookInput, StopHookInput, SubagentStartHookInput,
                SubagentStopHookInput, PreCompactHookInput, PermissionRequestHookInput {

    /**
     * Returns the hook event name for this input type.
     */
    String hookEventName();

    /**
     * Returns the session ID.
     */
    String sessionId();

    /**
     * Returns the transcript path.
     */
    String transcriptPath();

    /**
     * Returns the current working directory.
     */
    String cwd();

    /**
     * Returns the permission mode.
     */
    String permissionMode();
}
