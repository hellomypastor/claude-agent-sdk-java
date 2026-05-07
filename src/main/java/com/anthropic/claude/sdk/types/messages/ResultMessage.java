package com.anthropic.claude.sdk.types.messages;

import java.util.List;
import java.util.Map;

/**
 * Sealed interface for result messages indicating session completion.
 */
public sealed interface ResultMessage extends Message permits ResultSuccess, ResultError {

    String getSubtype();

    long getDurationMs();

    long getDurationApiMs();

    boolean isError();

    int getNumTurns();

    double getTotalCostUsd();

    Map<String, Object> getUsage();

    Map<String, ModelUsage> getModelUsage();

    List<PermissionDenial> getPermissionDenials();

    String getUuid();

    String getSessionId();
}
