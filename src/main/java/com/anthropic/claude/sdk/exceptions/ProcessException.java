package com.anthropic.claude.sdk.exceptions;

/**
 * Exception thrown when the CLI process fails or exits unexpectedly.
 */
public class ProcessException extends ClaudeSdkException {

    private final int exitCode;

    public ProcessException(String message, int exitCode) {
        super(message + " (exit code: " + exitCode + ")");
        this.exitCode = exitCode;
    }

    public ProcessException(String message, int exitCode, Throwable cause) {
        super(message + " (exit code: " + exitCode + ")", cause);
        this.exitCode = exitCode;
    }

    public int getExitCode() {
        return exitCode;
    }
}
