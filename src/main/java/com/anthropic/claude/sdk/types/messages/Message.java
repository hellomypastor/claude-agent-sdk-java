package com.anthropic.claude.sdk.types.messages;

/**
 * Base interface for all SDK message types.
 * Mirrors the TypeScript SDK's SDKMessage union type.
 * <p>
 * Dispatching is handled manually by {@link com.anthropic.claude.sdk.protocol.MessageParser},
 * not via Jackson polymorphic annotations, because multiple subtypes share the same
 * {@code type} value (e.g. "system", "result") and require secondary dispatch on {@code subtype}.
 */
public interface Message {

    /**
     * Get the type of this message.
     */
    String getType();
}
