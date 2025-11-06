package com.anthropic.claude.sdk.errors;

/**
 * Base exception class for all Claude SDK errors.
 */
public class ClaudeSDKException extends Exception {

    public ClaudeSDKException(String message) {
        super(message);
    }

    public ClaudeSDKException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClaudeSDKException(Throwable cause) {
        super(cause);
    }
}
