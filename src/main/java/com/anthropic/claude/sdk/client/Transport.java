package com.anthropic.claude.sdk.client;

import com.anthropic.claude.sdk.errors.ClaudeSDKException;

import java.io.BufferedReader;
import java.io.OutputStream;

/**
 * Interface for transport layer communication with Claude Code CLI.
 */
public interface Transport extends AutoCloseable {

    /**
     * Start the transport connection.
     *
     * @throws ClaudeSDKException if the connection fails
     */
    void start() throws ClaudeSDKException;

    /**
     * Get the output stream for sending data to the CLI.
     *
     * @return The output stream
     */
    OutputStream getOutputStream();

    /**
     * Get the buffered reader for receiving data from the CLI.
     *
     * @return The buffered reader
     */
    BufferedReader getReader();

    /**
     * Check if the transport is still alive.
     *
     * @return true if the transport is alive, false otherwise
     */
    boolean isAlive();

    /**
     * Get the exit code of the process (if applicable).
     *
     * @return The exit code, or null if not available
     */
    Integer getExitCode();

    /**
     * Close the transport connection.
     */
    @Override
    void close();
}
