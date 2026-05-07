package com.anthropic.claude.sdk.transport;

import java.util.List;
import java.util.Map;

/**
 * Options for spawning a subprocess.
 *
 * @param command the executable command
 * @param args    command-line arguments
 * @param cwd     working directory for the process
 * @param env     environment variables to set
 */
public record SpawnOptions(String command, List<String> args, String cwd, Map<String, String> env) {
}
