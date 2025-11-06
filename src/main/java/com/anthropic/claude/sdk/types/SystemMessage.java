package com.anthropic.claude.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Objects;

/**
 * A system message.
 */
public class SystemMessage implements Message {

    private final String subtype;
    private final Map<String, Object> data;

    @JsonCreator
    public SystemMessage(
            @JsonProperty("subtype") String subtype,
            @JsonProperty("data") Map<String, Object> data) {
        this.subtype = subtype;
        this.data = data;
    }

    @Override
    public String getRole() {
        return "system";
    }

    public String getSubtype() {
        return subtype;
    }

    public Map<String, Object> getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SystemMessage that = (SystemMessage) o;
        return Objects.equals(subtype, that.subtype) &&
               Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subtype, data);
    }

    @Override
    public String toString() {
        return "SystemMessage{subtype='" + subtype + "', data=" + data + "}";
    }
}
