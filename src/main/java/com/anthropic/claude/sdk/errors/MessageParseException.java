package com.anthropic.claude.sdk.errors;

import javax.annotation.Nullable;

/**
 * Raised when the SDK fails to interpret a message from CLI output.
 */
public class MessageParseException extends ClaudeSDKException {

    @Nullable
    private final Object data;

    public MessageParseException(String message) {
        this(message, null, null);
    }

    public MessageParseException(String message, @Nullable Object data) {
        this(message, data, null);
    }

    public MessageParseException(String message, @Nullable Object data, @Nullable Throwable cause) {
        super(message, cause);
        this.data = data;
    }

    @Nullable
    public Object getData() {
        return data;
    }
}
