package com.anthropic.claude.sdk.transport;

/**
 * Functional interface for spawning a subprocess.
 * Allows callers to provide custom process creation logic.
 */
@FunctionalInterface
public interface SpawnProcessFunction {

    /**
     * Spawn a process with the given options.
     *
     * @param options spawn configuration
     * @return the spawned process handle
     */
    SpawnedProcess spawn(SpawnOptions options);
}
