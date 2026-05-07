package com.anthropic.claude.sdk.types.hooks;

/**
 * Hook input for the PreCompact event.
 * Fired before context compaction occurs.
 */
public final class PreCompactHookInput extends BaseHookInput implements HookInput {

    private final String trigger;
    private final String customInstructions;

    public PreCompactHookInput(String sessionId, String transcriptPath, String cwd, String permissionMode,
                                String trigger, String customInstructions) {
        super(sessionId, transcriptPath, cwd, permissionMode);
        this.trigger = trigger;
        this.customInstructions = customInstructions;
    }

    public String trigger() {
        return trigger;
    }

    public String customInstructions() {
        return customInstructions;
    }

    @Override
    public String hookEventName() {
        return "PreCompact";
    }
}
