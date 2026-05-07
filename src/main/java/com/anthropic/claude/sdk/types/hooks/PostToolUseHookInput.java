package com.anthropic.claude.sdk.types.hooks;

/**
 * Hook input for the PostToolUse event.
 * Fired after a tool has been executed successfully.
 */
public final class PostToolUseHookInput extends BaseHookInput implements HookInput {

    private final String toolName;
    private final Object toolInput;
    private final Object toolResponse;
    private final String toolUseId;

    public PostToolUseHookInput(String sessionId, String transcriptPath, String cwd, String permissionMode,
                                String toolName, Object toolInput, Object toolResponse, String toolUseId) {
        super(sessionId, transcriptPath, cwd, permissionMode);
        this.toolName = toolName;
        this.toolInput = toolInput;
        this.toolResponse = toolResponse;
        this.toolUseId = toolUseId;
    }

    public String toolName() {
        return toolName;
    }

    public Object toolInput() {
        return toolInput;
    }

    public Object toolResponse() {
        return toolResponse;
    }

    public String toolUseId() {
        return toolUseId;
    }

    @Override
    public String hookEventName() {
        return "PostToolUse";
    }
}
