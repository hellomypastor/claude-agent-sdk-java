package com.anthropic.claude.sdk.exceptions;

/**
 * Exception thrown when message parsing fails.
 */
public class MessageParseException extends ClaudeSdkException {

    private final String rawData;

    public MessageParseException(String message, String rawData) {
        super(message);
        this.rawData = rawData;
    }

    public MessageParseException(String message, String rawData, Throwable cause) {
        super(message, cause);
        this.rawData = rawData;
    }

    public String getRawData() {
        return rawData;
    }
}
