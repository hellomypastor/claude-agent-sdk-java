package com.anthropic.claude.sdk.types.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * System message (internal SDK messages).
 */
@Data
@AllArgsConstructor
public final class SystemMessage implements Message {
    @JsonProperty("subtype")
    private final String subtype;

    @JsonProperty("data")
    private final Map<String, Object> data;

    @Override
    public String getType() {
        return "system";
    }
}
