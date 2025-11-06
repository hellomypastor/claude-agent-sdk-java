package com.anthropic.claude.sdk.types.permissions;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Permission update for future tool uses.
 */
@Data
@AllArgsConstructor
public final class PermissionUpdate {
    private final String tool;
    private final String behavior;
    private final Integer turns;
}
