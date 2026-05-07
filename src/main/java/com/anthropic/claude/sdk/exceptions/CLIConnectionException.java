package com.anthropic.claude.sdk.exceptions;

/**
 * Exception thrown when connection to Claude Code CLI fails.
 */
public class CLIConnectionException extends ClaudeSdkException {

    public CLIConnectionException(String message) {
        super(message);
    }

    public CLIConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
