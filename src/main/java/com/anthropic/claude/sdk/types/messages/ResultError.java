package com.anthropic.claude.sdk.types.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * Result message indicating session completion with an error.
 */
public record ResultError(
        @JsonProperty("subtype") String subtype,
        @JsonProperty("duration_ms") long durationMs,
        @JsonProperty("duration_api_ms") long durationApiMs,
        @JsonProperty("is_error") boolean isError,
        @JsonProperty("num_turns") int numTurns,
        @JsonProperty("total_cost_usd") double totalCostUsd,
        @JsonProperty("usage") Map<String, Object> usage,
        @JsonProperty("model_usage") Map<String, ModelUsage> modelUsage,
        @JsonProperty("permission_denials") List<PermissionDenial> permissionDenials,
        @JsonProperty("errors") List<String> errors,
        @JsonProperty("uuid") String uuid,
        @JsonProperty("session_id") String sessionId
) implements ResultMessage {

    @Override
    public String getType() {
        return "result";
    }

    @Override
    public String getSubtype() {
        return subtype;
    }

    @Override
    public long getDurationMs() {
        return durationMs;
    }

    @Override
    public long getDurationApiMs() {
        return durationApiMs;
    }

    @Override
    public int getNumTurns() {
        return numTurns;
    }

    @Override
    public double getTotalCostUsd() {
        return totalCostUsd;
    }

    @Override
    public Map<String, Object> getUsage() {
        return usage;
    }

    @Override
    public Map<String, ModelUsage> getModelUsage() {
        return modelUsage;
    }

    @Override
    public List<PermissionDenial> getPermissionDenials() {
        return permissionDenials;
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }
}
