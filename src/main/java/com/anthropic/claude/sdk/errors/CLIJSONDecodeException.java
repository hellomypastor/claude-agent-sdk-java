package com.anthropic.claude.sdk.errors;

import javax.annotation.Nullable;

/**
 * Thrown when the SDK cannot parse JSON from CLI output.
 */
public class CLIJSONDecodeException extends ClaudeSDKException {

    @Nullable
    private final String problematicLine;

    public CLIJSONDecodeException(String message, @Nullable String problematicLine, Throwable cause) {
        super(message, cause);
        this.problematicLine = problematicLine;
    }

    public CLIJSONDecodeException(String message, Throwable cause) {
        this(message, null, cause);
    }

    @Nullable
    public String getProblematicLine() {
        return problematicLine;
    }
}
