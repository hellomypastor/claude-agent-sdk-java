package com.anthropic.claude.sdk.client;

import com.anthropic.claude.sdk.errors.CLINotFoundException;
import com.anthropic.claude.sdk.errors.ClaudeSDKException;
import com.anthropic.claude.sdk.errors.ProcessException;
import com.anthropic.claude.sdk.types.ClaudeAgentOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Transport implementation using a subprocess to communicate with Claude Code CLI.
 */
public class ProcessTransport implements Transport {

    private static final Logger logger = LoggerFactory.getLogger(ProcessTransport.class);

    private final ClaudeAgentOptions options;
    @Nullable
    private Process process;
    @Nullable
    private BufferedReader reader;

    public ProcessTransport(@Nullable ClaudeAgentOptions options) {
        this.options = options != null ? options : ClaudeAgentOptions.builder().build();
    }

    @Override
    public void start() throws ClaudeSDKException {
        String cliPath = findClaudeCLI();
        List<String> command = buildCommand(cliPath);

        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            if (options.getCwd() != null) {
                pb.directory(options.getCwd().toFile());
            }

            // Set environment variable
            pb.environment().put("CLAUDE_CODE_ENTRYPOINT", "sdk-java");

            logger.debug("Starting Claude Code CLI: {}", String.join(" ", command));
            process = pb.start();
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        } catch (IOException e) {
            throw new ClaudeSDKException("Failed to start Claude Code CLI", e);
        }
    }

    private String findClaudeCLI() throws CLINotFoundException {
        // Try common locations
        String[] possiblePaths = {
                "claude-code",
                "/usr/local/bin/claude-code",
                System.getProperty("user.home") + "/.npm/bin/claude-code",
                System.getProperty("user.home") + "/.nvm/versions/node/*/bin/claude-code"
        };

        for (String path : possiblePaths) {
            if (path.contains("*")) {
                // Handle glob patterns (simplified)
                continue;
            }
            if (isExecutable(path)) {
                return path;
            }
        }

        // Try to find via 'which' command on Unix-like systems
        try {
            Process which = Runtime.getRuntime().exec(new String[]{"which", "claude-code"});
            BufferedReader reader = new BufferedReader(new InputStreamReader(which.getInputStream()));
            String line = reader.readLine();
            if (line != null && !line.isEmpty() && isExecutable(line)) {
                return line;
            }
        } catch (IOException ignored) {
        }

        throw new CLINotFoundException(null);
    }

    private boolean isExecutable(String path) {
        try {
            Path p = Paths.get(path);
            return Files.exists(p) && Files.isExecutable(p);
        } catch (Exception e) {
            return false;
        }
    }

    private List<String> buildCommand(String cliPath) {
        List<String> command = new ArrayList<>();
        command.add(cliPath);
        command.add("sdk");
        command.add("--streaming");

        return command;
    }

    @Override
    public OutputStream getOutputStream() {
        if (process == null) {
            throw new IllegalStateException("Transport not started");
        }
        return process.getOutputStream();
    }

    @Override
    public BufferedReader getReader() {
        if (reader == null) {
            throw new IllegalStateException("Transport not started");
        }
        return reader;
    }

    @Override
    public boolean isAlive() {
        return process != null && process.isAlive();
    }

    @Override
    @Nullable
    public Integer getExitCode() {
        if (process == null || process.isAlive()) {
            return null;
        }
        return process.exitValue();
    }

    @Override
    public void close() {
        if (process != null) {
            process.destroy();
            try {
                process.waitFor();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                logger.warn("Error closing reader", e);
            }
        }
    }
}
