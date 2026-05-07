package com.anthropic.claude.sdk.exceptions;

/**
 * Base exception for all Claude SDK errors.
 */
public class ClaudeSdkException extends RuntimeException {

    public ClaudeSdkException(String message) {
        super(message);
    }

    public ClaudeSdkException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClaudeSdkException(Throwable cause) {
        super(cause);
    }
}
