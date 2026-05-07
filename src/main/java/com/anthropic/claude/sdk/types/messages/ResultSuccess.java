package com.anthropic.claude.sdk.types.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * Result message indicating successful session completion.
 */
public final class ResultSuccess implements ResultMessage {

    private final long durationMs;
    private final long durationApiMs;
    private final boolean isError;
    private final int numTurns;
    private final String result;
    private final double totalCostUsd;
    private final Map<String, Object> usage;
    private final Map<String, ModelUsage> modelUsage;
    private final List<PermissionDenial> permissionDenials;
    private final Object structuredOutput;
    private final String uuid;
    private final String sessionId;

    public ResultSuccess(
            @JsonProperty("duration_ms") long durationMs,
            @JsonProperty("duration_api_ms") long durationApiMs,
            @JsonProperty("is_error") boolean isError,
            @JsonProperty("num_turns") int numTurns,
            @JsonProperty("result") String result,
            @JsonProperty("total_cost_usd") double totalCostUsd,
            @JsonProperty("usage") Map<String, Object> usage,
            @JsonProperty("model_usage") Map<String, ModelUsage> modelUsage,
            @JsonProperty("permission_denials") List<PermissionDenial> permissionDenials,
            @JsonProperty("structured_output") Object structuredOutput,
            @JsonProperty("uuid") String uuid,
            @JsonProperty("session_id") String sessionId
    ) {
        this.durationMs = durationMs;
        this.durationApiMs = durationApiMs;
        this.isError = isError;
        this.numTurns = numTurns;
        this.result = result;
        this.totalCostUsd = totalCostUsd;
        this.usage = usage;
        this.modelUsage = modelUsage;
        this.permissionDenials = permissionDenials;
        this.structuredOutput = structuredOutput;
        this.uuid = uuid;
        this.sessionId = sessionId;
    }

    @JsonProperty("duration_ms")
    public long durationMs() {
        return durationMs;
    }

    @JsonProperty("duration_api_ms")
    public long durationApiMs() {
        return durationApiMs;
    }

    @JsonProperty("is_error")
    public boolean isError() {
        return isError;
    }

    @JsonProperty("num_turns")
    public int numTurns() {
        return numTurns;
    }

    @JsonProperty("result")
    public String result() {
        return result;
    }


    @JsonProperty("total_cost_usd")
    public double totalCostUsd() {
        return totalCostUsd;
    }

    @JsonProperty("usage")
    public Map<String, Object> usage() {
        return usage;
    }

    @JsonProperty("model_usage")
    public Map<String, ModelUsage> modelUsage() {
        return modelUsage;
    }

    @JsonProperty("permission_denials")
    public List<PermissionDenial> permissionDenials() {
        return permissionDenials;
    }

    @JsonProperty("structured_output")
    public Object structuredOutput() {
        return structuredOutput;
    }

    @JsonProperty("uuid")
    public String uuid() {
        return uuid;
    }

    @JsonProperty("session_id")
    public String sessionId() {
        return sessionId;
    }

    @Override
    public String getType() {
        return "result";
    }

    @Override
    public String getSubtype() {
        return "success";
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
