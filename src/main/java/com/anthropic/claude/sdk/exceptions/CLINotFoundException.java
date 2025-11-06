package com.anthropic.claude.sdk.exceptions;

/**
 * Exception thrown when Claude Code CLI cannot be found.
 */
public class CLINotFoundException extends ClaudeSdkException {

    public CLINotFoundException(String message) {
        super(message);
    }

    public CLINotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
