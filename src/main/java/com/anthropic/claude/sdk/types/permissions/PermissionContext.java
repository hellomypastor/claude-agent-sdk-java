package com.anthropic.claude.sdk.types.permissions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Context provided to permission callbacks.
 */
@Data
@AllArgsConstructor
public final class PermissionContext {
    private final Object signal;                       // For cancellation support (future)
    private final List<String> suggestions;            // Suggested permission responses
}
