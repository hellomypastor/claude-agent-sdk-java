package com.anthropic.claude.sdk.types.messages;

import java.util.List;
import java.util.Map;

/**
 * Interface for result messages indicating session completion.
 */
public interface ResultMessage extends Message {

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
