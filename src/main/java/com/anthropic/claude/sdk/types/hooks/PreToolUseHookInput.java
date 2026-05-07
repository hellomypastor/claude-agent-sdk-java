package com.anthropic.claude.sdk.types.hooks;

/**
 * Hook input for the PreToolUse event.
 * Fired before a tool is executed, allowing inspection or modification of tool input.
 */
public final class PreToolUseHookInput extends BaseHookInput implements HookInput {

    private final String toolName;
    private final Object toolInput;
    private final String toolUseId;

    public PreToolUseHookInput(String sessionId, String transcriptPath, String cwd, String permissionMode,
                               String toolName, Object toolInput, String toolUseId) {
        super(sessionId, transcriptPath, cwd, permissionMode);
        this.toolName = toolName;
        this.toolInput = toolInput;
        this.toolUseId = toolUseId;
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

    @Override
    public String hookEventName() {
        return "PreToolUse";
    }
}
