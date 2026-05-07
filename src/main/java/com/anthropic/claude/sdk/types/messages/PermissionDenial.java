package com.anthropic.claude.sdk.types.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Record of a permission denial during session execution.
 */
public record PermissionDenial(
        @JsonProperty("tool_name") String toolName,
        @JsonProperty("tool_use_id") String toolUseId,
        @JsonProperty("tool_input") Map<String, Object> toolInput
) {}
