package com.anthropic.claude.sdk.transport;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Abstraction over a spawned subprocess, providing access to its I/O streams
 * and lifecycle management.
 */
public interface SpawnedProcess {

    /**
     * Returns the output stream connected to the process's stdin.
     */
    OutputStream stdin();

    /**
     * Returns the input stream connected to the process's stdout.
     */
    InputStream stdout();

    /**
     * Returns whether the process has been killed.
     */
    boolean isKilled();

    /**
     * Returns the process exit code, or null if still running.
     */
    Integer exitCode();

    /**
     * Send a signal to kill the process.
     *
     * @param signal the signal name (e.g. "SIGTERM", "SIGKILL")
     */
    void kill(String signal);

    /**
     * Register a listener for process exit.
     *
     * @param listener receives the exit code and optional error message
     */
    void onExit(BiConsumer<Integer, String> listener);

    /**
     * Register a listener for process errors.
     *
     * @param listener receives the error
     */
    void onError(Consumer<Throwable> listener);
}
