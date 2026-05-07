package com.anthropic.claude.sdk.types.hooks;

/**
 * Hook input for the PostToolUseFailure event.
 * Fired after a tool execution has failed.
 */
public final class PostToolUseFailureHookInput extends BaseHookInput implements HookInput {

    private final String toolName;
    private final Object toolInput;
    private final String toolUseId;
    private final String error;
    private final Boolean isInterrupt;

    public PostToolUseFailureHookInput(String sessionId, String transcriptPath, String cwd, String permissionMode,
                                       String toolName, Object toolInput, String toolUseId,
                                       String error, Boolean isInterrupt) {
        super(sessionId, transcriptPath, cwd, permissionMode);
        this.toolName = toolName;
        this.toolInput = toolInput;
        this.toolUseId = toolUseId;
        this.error = error;
        this.isInterrupt = isInterrupt;
    }

    public String toolName() {
        return toolName;
    }

    public Object toolInput() {
        return toolInput;
    }

    public String toolUseId() {
        return toolUseId;
    }

    public String error() {
        return error;
    }

    public Boolean isInterrupt() {
        return isInterrupt;
    }

    @Override
    public String hookEventName() {
        return "PostToolUseFailure";
    }
}
