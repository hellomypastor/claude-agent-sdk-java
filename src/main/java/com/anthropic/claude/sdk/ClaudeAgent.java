package com.anthropic.claude.sdk;

import com.anthropic.claude.sdk.client.ClaudeSDKClient;
import com.anthropic.claude.sdk.client.ProcessTransport;
import com.anthropic.claude.sdk.client.Transport;
import com.anthropic.claude.sdk.errors.ClaudeSDKException;
import com.anthropic.claude.sdk.types.ClaudeAgentOptions;
import com.anthropic.claude.sdk.types.Message;

import javax.annotation.Nullable;
import java.util.Iterator;

/**
 * Main entry point for the Claude Agent SDK.
 * Provides convenience methods for querying Claude.
 */
public class ClaudeAgent {

    /**
     * Query Claude Code for one-shot or unidirectional streaming interactions.
     *
     * @param prompt The prompt to send to Claude
     * @param options Configuration options (null for defaults)
     * @param transport Custom transport (null for default process transport)
     * @return An iterator of messages from Claude
     * @throws ClaudeSDKException if the query fails
     */
    public static Iterator<Message> query(
            String prompt,
            @Nullable ClaudeAgentOptions options,
            @Nullable Transport transport) throws ClaudeSDKException {

        ClaudeAgentOptions opts = options != null ? options : ClaudeAgentOptions.builder().build();
        ClaudeSDKClient client = new ClaudeSDKClient(opts, transport);

        client.connect(prompt);
        return client.receiveMessages();
    }

    /**
     * Query Claude Code with default options.
     *
     * @param prompt The prompt to send to Claude
     * @return An iterator of messages from Claude
     * @throws ClaudeSDKException if the query fails
     */
    public static Iterator<Message> query(String prompt) throws ClaudeSDKException {
        return query(prompt, null, null);
    }

    /**
     * Query Claude Code with custom options.
     *
     * @param prompt The prompt to send to Claude
     * @param options Configuration options
     * @return An iterator of messages from Claude
     * @throws ClaudeSDKException if the query fails
     */
    public static Iterator<Message> query(String prompt, ClaudeAgentOptions options) throws ClaudeSDKException {
        return query(prompt, options, null);
    }

    /**
     * Creates a new Claude SDK client for bidirectional communication.
     *
     * @param options Configuration options (null for defaults)
     * @return A new ClaudeSDKClient instance
     */
    public static ClaudeSDKClient createClient(@Nullable ClaudeAgentOptions options) {
        return new ClaudeSDKClient(options, null);
    }

    /**
     * Creates a new Claude SDK client with default options.
     *
     * @return A new ClaudeSDKClient instance
     */
    public static ClaudeSDKClient createClient() {
        return new ClaudeSDKClient();
    }
}
