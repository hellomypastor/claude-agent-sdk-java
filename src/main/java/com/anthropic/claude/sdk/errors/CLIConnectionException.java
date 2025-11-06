package com.anthropic.claude.sdk.errors;

/**
 * Raised when connection to Claude Code fails.
 */
public class CLIConnectionException extends ClaudeSDKException {

    public CLIConnectionException(String message) {
        super(message);
    }

    public CLIConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
