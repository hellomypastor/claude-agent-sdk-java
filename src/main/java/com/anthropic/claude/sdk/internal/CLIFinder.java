package com.anthropic.claude.sdk.internal;

import com.anthropic.claude.sdk.exceptions.CLINotFoundException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for finding Claude Code CLI executable.
 * Mirrors the Python SDK's CLI finding logic.
 */
public class CLIFinder {

    private static final String CLI_NAME = "claude";
    private static final String MINIMUM_VERSION = "2.0.0";

    /**
     * Find the Claude Code CLI executable.
     * Searches in PATH and common installation locations.
     *
     * @return Path to the CLI executable
     * @throws CLINotFoundException if CLI cannot be found
     */
    public static String findCLI() {
        // First, try to find in PATH
        String cliFromPath = findInPath();
        if (cliFromPath != null) {
            return cliFromPath;
        }

        // Search in common installation locations
        List<Path> locations = getCommonLocations();
        for (Path location : locations) {
            if (Files.exists(location) && Files.isRegularFile(location)) {
                return location.toString();
            }
        }

        // Not found - throw exception with helpful message
        throw new CLINotFoundException(
                "Claude Code CLI not found. Install with:\n" +
                        "  npm install -g @anthropic-ai/claude-code\n" +
                        "\n" +
                        "If already installed locally, try:\n" +
                        "  export PATH=\"$HOME/node_modules/.bin:$PATH\"\n" +
                        "\n" +
                        "Or provide the path via ClaudeAgentOptions:\n" +
                        "  ClaudeAgentOptions.builder().cliPath(Path.of(\"/path/to/claude\")).build()"
        );
    }

    /**
     * Find CLI in system PATH.
     */
    private static String findInPath() {
        String pathEnv = System.getenv("PATH");
        if (pathEnv == null) {
            return null;
        }

        String[] paths = pathEnv.split(File.pathSeparator);
        for (String dir : paths) {
            Path cliPath = Paths.get(dir, CLI_NAME);
            if (Files.exists(cliPath) && Files.isRegularFile(cliPath)) {
                return cliPath.toString();
            }
        }

        return null;
    }

    /**
     * Get list of common installation locations.
     */
    private static List<Path> getCommonLocations() {
        String homeDir = System.getProperty("user.home");
        List<Path> locations = new ArrayList<>();

        // Common npm/yarn installation paths
        locations.add(Paths.get(homeDir, ".npm-global", "bin", CLI_NAME));
        locations.add(Paths.get("/usr", "local", "bin", CLI_NAME));
        locations.add(Paths.get(homeDir, ".local", "bin", CLI_NAME));
        locations.add(Paths.get(homeDir, "node_modules", ".bin", CLI_NAME));
        locations.add(Paths.get(homeDir, ".yarn", "bin", CLI_NAME));
        locations.add(Paths.get(homeDir, ".claude", "local", CLI_NAME));

        return locations;
    }
}
