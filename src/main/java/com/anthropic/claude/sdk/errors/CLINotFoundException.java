package com.anthropic.claude.sdk.errors;

import javax.annotation.Nullable;

/**
 * Triggered when Claude Code is not installed or unavailable.
 */
public class CLINotFoundException extends ClaudeSDKException {

    @Nullable
    private final String cliPath;

    public CLINotFoundException(@Nullable String cliPath) {
        super(buildMessage(cliPath));
        this.cliPath = cliPath;
    }

    private static String buildMessage(@Nullable String cliPath) {
        if (cliPath != null) {
            return "Claude Code CLI not found at: " + cliPath;
        }
        return "Claude Code CLI not found. Please install it with: npm install -g @anthropic-ai/claude-code";
    }

    @Nullable
    public String getCliPath() {
        return cliPath;
    }
}
