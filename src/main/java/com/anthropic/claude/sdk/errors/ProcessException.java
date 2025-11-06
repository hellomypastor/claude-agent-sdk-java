package com.anthropic.claude.sdk.errors;

import javax.annotation.Nullable;

/**
 * Occurs when the CLI process encounters a failure.
 * Captures exit code and stderr output.
 */
public class ProcessException extends ClaudeSDKException {

    private final int exitCode;
    @Nullable
    private final String stderr;

    public ProcessException(int exitCode, @Nullable String stderr) {
        super(buildMessage(exitCode, stderr));
        this.exitCode = exitCode;
        this.stderr = stderr;
    }

    private static String buildMessage(int exitCode, @Nullable String stderr) {
        StringBuilder sb = new StringBuilder();
        sb.append("CLI process failed with exit code ").append(exitCode);
        if (stderr != null && !stderr.isEmpty()) {
            sb.append(": ").append(stderr);
        }
        return sb.toString();
    }

    public int getExitCode() {
        return exitCode;
    }

    @Nullable
    public String getStderr() {
        return stderr;
    }
}
