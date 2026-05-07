package com.anthropic.claude.sdk.types.hooks;

/**
 * Interface for all hook input types.
 * Each hook event has a corresponding input type that carries event-specific data.
 */
public interface HookInput {

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
