package com.anthropic.claude.sdk;

/**
 * Information about an available model.
 *
 * @param value       the model identifier
 * @param displayName the human-readable model name
 * @param description a brief description of the model
 */
public record ModelInfo(
        String value,
        String displayName,
        String description
) {
}
