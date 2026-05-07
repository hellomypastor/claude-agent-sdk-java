package com.anthropic.claude.sdk.transport;

import java.util.List;
import java.util.Map;

/**
 * Options for spawning a subprocess.
 */
public final class SpawnOptions {

    private final String command;
    private final List<String> args;
    private final String cwd;
    private final Map<String, String> env;

    public SpawnOptions(String command, List<String> args, String cwd, Map<String, String> env) {
        this.command = command;
        this.args = args;
        this.cwd = cwd;
        this.env = env;
    }

    public String command() {
        return command;
    }

    public List<String> args() {
        return args;
    }

    public String cwd() {
        return cwd;
    }

    public Map<String, String> env() {
        return env;
    }
}
