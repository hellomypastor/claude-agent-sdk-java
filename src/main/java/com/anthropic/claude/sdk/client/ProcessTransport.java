package com.anthropic.claude.sdk.client;

import com.anthropic.claude.sdk.errors.CLINotFoundException;
import com.anthropic.claude.sdk.errors.ClaudeSDKException;
import com.anthropic.claude.sdk.errors.ProcessException;
import com.anthropic.claude.sdk.types.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Transport implementation using a subprocess to communicate with Claude Code CLI.
 */
public class ProcessTransport implements Transport {

    private static final Logger logger = LoggerFactory.getLogger(ProcessTransport.class);
    private static final int CMD_LENGTH_LIMIT_WINDOWS = 8000;
    private static final int CMD_LENGTH_LIMIT_OTHER = 100000;
    private static final String MINIMUM_CLAUDE_CODE_VERSION = "2.0.0";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final ClaudeAgentOptions options;
    @Nullable
    private Process process;
    @Nullable
    private BufferedReader reader;
    private final List<Path> tempFiles = new ArrayList<>();
    @Nullable
    private final String prompt;
    private final boolean isStreaming;

    public ProcessTransport(@Nullable ClaudeAgentOptions options) {
        this(null, options, false);
    }

    public ProcessTransport(@Nullable String prompt, @Nullable ClaudeAgentOptions options, boolean isStreaming) {
        this.prompt = prompt;
        this.options = options != null ? options : ClaudeAgentOptions.builder().build();
        this.isStreaming = isStreaming;
    }

    @Override
    public void start() throws ClaudeSDKException {
        String cliPath = determineCliPath();

        // Check Claude Code version before starting
        checkClaudeVersion(cliPath);

        List<String> command = buildCommand(cliPath);

        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            if (options.getCwd() != null) {
                pb.directory(options.getCwd().toFile());
            }

            // Set environment variables
            pb.environment().put("CLAUDE_CODE_ENTRYPOINT", "sdk-java");

            // Add custom environment variables from options
            if (options.getEnv() != null) {
                pb.environment().putAll(options.getEnv());
            }

            logger.debug("Starting Claude Code CLI: {}", String.join(" ", command));
            process = pb.start();
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        } catch (IOException e) {
            throw new ClaudeSDKException("Failed to start Claude Code CLI", e);
        }
    }

    /**
     * Determines the CLI path to use.
     * First checks if cliPath was explicitly provided in options,
     * otherwise searches for the CLI in common locations.
     *
     * @return the path to the Claude CLI executable
     * @throws CLINotFoundException if CLI cannot be found
     */
    private String determineCliPath() throws CLINotFoundException {
        // If user provided explicit CLI path, use it
        if (options.getCliPath() != null) {
            String explicitPath = options.getCliPath().toString();
            if (isExecutable(explicitPath)) {
                logger.debug("Using explicitly configured CLI path: {}", explicitPath);
                return explicitPath;
            } else {
                throw new CLINotFoundException(explicitPath);
            }
        }

        // Otherwise search for CLI
        return findClaudeCLI();
    }

    private String findClaudeCLI() throws CLINotFoundException {
        // First, try to find in PATH using 'which' command (like shutil.which in Python)
        try {
            Process which = Runtime.getRuntime().exec(new String[]{"which", "claude"});
            BufferedReader whichReader = new BufferedReader(new InputStreamReader(which.getInputStream()));
            String line = whichReader.readLine();
            whichReader.close();
            which.waitFor();

            if (line != null && !line.isEmpty()) {
                Path p = Paths.get(line.trim());
                if (Files.exists(p) && Files.isRegularFile(p)) {
                    return line.trim();
                }
            }
        } catch (IOException | InterruptedException e) {
            // Continue to fallback locations
        }

        // Fallback to checking common installation locations (matching Python SDK exactly)
        String homeDir = System.getProperty("user.home");
        String[] possiblePaths = {
                homeDir + "/.npm-global/bin/claude",
                "/usr/local/bin/claude",
                homeDir + "/.local/bin/claude",
                homeDir + "/node_modules/.bin/claude",
                homeDir + "/.yarn/bin/claude",
                homeDir + "/.claude/local/claude"
        };

        for (String path : possiblePaths) {
            if (isExecutable(path)) {
                return path;
            }
        }

        throw new CLINotFoundException(null);
    }

    private boolean isExecutable(String path) {
        try {
            Path p = Paths.get(path);
            return Files.exists(p) && Files.isRegularFile(p) && Files.isExecutable(p);
        } catch (Exception e) {
            return false;
        }
    }

    private List<String> buildCommand(String cliPath) throws ClaudeSDKException {
        List<String> cmd = new ArrayList<>();
        cmd.add(cliPath);
        cmd.add("--output-format");
        cmd.add("stream-json");
        cmd.add("--verbose");

        // System prompt handling
        Object systemPrompt = options.getSystemPrompt();
        if (systemPrompt == null) {
            cmd.add("--system-prompt");
            cmd.add("");
        } else if (systemPrompt instanceof String) {
            cmd.add("--system-prompt");
            cmd.add((String) systemPrompt);
        } else if (systemPrompt instanceof SystemPromptPreset) {
            SystemPromptPreset preset = (SystemPromptPreset) systemPrompt;
            if ("preset".equals(preset.getType()) && preset.getAppend() != null) {
                cmd.add("--append-system-prompt");
                cmd.add(preset.getAppend());
            }
        }

        // Allowed tools
        if (options.getAllowedTools() != null && !options.getAllowedTools().isEmpty()) {
            cmd.add("--allowedTools");
            cmd.add(String.join(",", options.getAllowedTools()));
        }

        // Max turns
        if (options.getMaxTurns() != null) {
            cmd.add("--max-turns");
            cmd.add(String.valueOf(options.getMaxTurns()));
        }

        // Max budget USD
        if (options.getMaxBudgetUsd() != null) {
            cmd.add("--max-budget-usd");
            cmd.add(String.valueOf(options.getMaxBudgetUsd()));
        }

        // Disallowed tools
        if (options.getDisallowedTools() != null && !options.getDisallowedTools().isEmpty()) {
            cmd.add("--disallowedTools");
            cmd.add(String.join(",", options.getDisallowedTools()));
        }

        // Model
        if (options.getModel() != null) {
            cmd.add("--model");
            cmd.add(options.getModel());
        }

        // Permission prompt tool name
        if (options.getPermissionPromptToolName() != null) {
            cmd.add("--permission-prompt-tool");
            cmd.add(options.getPermissionPromptToolName());
        }

        // Permission mode
        if (options.getPermissionMode() != null) {
            cmd.add("--permission-mode");
            cmd.add(options.getPermissionMode());
        }

        // Continue conversation
        if (options.isContinueConversation()) {
            cmd.add("--continue");
        }

        // Resume session
        if (options.getResume() != null) {
            cmd.add("--resume");
            cmd.add(options.getResume());
        }

        // Settings
        if (options.getSettings() != null) {
            cmd.add("--settings");
            cmd.add(options.getSettings());
        }

        // Add directories
        if (options.getAddDirs() != null && !options.getAddDirs().isEmpty()) {
            for (Path dir : options.getAddDirs()) {
                cmd.add("--add-dir");
                cmd.add(dir.toString());
            }
        }

        // MCP servers
        if (options.getMcpServers() != null) {
            addMcpServersToCommand(cmd, options.getMcpServers());
        }

        // Include partial messages
        if (options.isIncludePartialMessages()) {
            cmd.add("--include-partial-messages");
        }

        // Fork session
        if (options.isForkSession()) {
            cmd.add("--fork-session");
        }

        // Agents
        String agentsJson = null;
        if (options.getAgents() != null && !options.getAgents().isEmpty()) {
            agentsJson = serializeAgents(options.getAgents());
            cmd.add("--agents");
            cmd.add(agentsJson);
        }

        // Setting sources
        String sources = "";
        if (options.getSettingSources() != null && !options.getSettingSources().isEmpty()) {
            sources = options.getSettingSources().stream()
                    .map(SettingSource::getValue)
                    .collect(Collectors.joining(","));
        }
        cmd.add("--setting-sources");
        cmd.add(sources);

        // Plugins
        if (options.getPlugins() != null && !options.getPlugins().isEmpty()) {
            for (SdkPluginConfig plugin : options.getPlugins()) {
                if ("local".equals(plugin.getType())) {
                    cmd.add("--plugin-dir");
                    cmd.add(plugin.getPath());
                } else {
                    throw new ClaudeSDKException("Unsupported plugin type: " + plugin.getType());
                }
            }
        }

        // Extra args
        if (options.getExtraArgs() != null && !options.getExtraArgs().isEmpty()) {
            for (Map.Entry<String, String> entry : options.getExtraArgs().entrySet()) {
                if (entry.getValue() == null) {
                    // Boolean flag without value
                    cmd.add("--" + entry.getKey());
                } else {
                    // Flag with value
                    cmd.add("--" + entry.getKey());
                    cmd.add(entry.getValue());
                }
            }
        }

        // Prompt handling
        if (isStreaming) {
            cmd.add("--input-format");
            cmd.add("stream-json");
        } else if (prompt != null) {
            cmd.add("--print");
            cmd.add("--");
            cmd.add(prompt);
        }

        // Max thinking tokens
        if (options.getMaxThinkingTokens() != null) {
            cmd.add("--max-thinking-tokens");
            cmd.add(String.valueOf(options.getMaxThinkingTokens()));
        }

        // Check command line length (Windows limitation)
        String cmdStr = String.join(" ", cmd);
        int cmdLengthLimit = isWindows() ? CMD_LENGTH_LIMIT_WINDOWS : CMD_LENGTH_LIMIT_OTHER;

        if (cmdStr.length() > cmdLengthLimit && agentsJson != null) {
            // Command too long - use temp file for agents
            try {
                int agentsIdx = cmd.indexOf("--agents");
                if (agentsIdx >= 0 && agentsIdx + 1 < cmd.size()) {
                    // Create temporary file
                    Path tempFile = Files.createTempFile("claude-agents-", ".json");
                    Files.writeString(tempFile, agentsJson);
                    tempFiles.add(tempFile);

                    // Replace JSON with @filepath reference
                    cmd.set(agentsIdx + 1, "@" + tempFile.toAbsolutePath());

                    logger.info("Command line length ({}) exceeds limit ({}). Using temp file for --agents: {}",
                            cmdStr.length(), cmdLengthLimit, tempFile);
                }
            } catch (IOException e) {
                logger.warn("Failed to optimize command line length: {}", e.getMessage());
            }
        }

        return cmd;
    }

    private void addMcpServersToCommand(List<String> cmd, Object mcpServers) throws ClaudeSDKException {
        if (mcpServers instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> serversMap = (Map<String, Object>) mcpServers;

            // Process all servers, stripping instance field from SDK servers
            Map<String, Object> serversForCli = new HashMap<>();
            for (Map.Entry<String, Object> entry : serversMap.entrySet()) {
                String name = entry.getKey();
                Object config = entry.getValue();

                if (config instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> configMap = (Map<String, Object>) config;

                    if ("sdk".equals(configMap.get("type"))) {
                        // For SDK servers, pass everything except the instance field
                        Map<String, Object> sdkConfig = new HashMap<>(configMap);
                        sdkConfig.remove("instance");
                        serversForCli.put(name, sdkConfig);
                    } else {
                        // For external servers, pass as-is
                        serversForCli.put(name, config);
                    }
                } else {
                    serversForCli.put(name, config);
                }
            }

            // Pass all servers to CLI
            if (!serversForCli.isEmpty()) {
                try {
                    Map<String, Object> wrapper = new HashMap<>();
                    wrapper.put("mcpServers", serversForCli);
                    String json = objectMapper.writeValueAsString(wrapper);
                    cmd.add("--mcp-config");
                    cmd.add(json);
                } catch (JsonProcessingException e) {
                    throw new ClaudeSDKException("Failed to serialize MCP servers", e);
                }
            }
        } else if (mcpServers instanceof String || mcpServers instanceof Path) {
            // String or Path format: pass directly as file path
            cmd.add("--mcp-config");
            cmd.add(mcpServers.toString());
        }
    }

    private String serializeAgents(Map<String, AgentDefinition> agents) throws ClaudeSDKException {
        try {
            Map<String, Map<String, Object>> agentsMap = new HashMap<>();
            for (Map.Entry<String, AgentDefinition> entry : agents.entrySet()) {
                AgentDefinition agent = entry.getValue();
                Map<String, Object> agentMap = new HashMap<>();

                agentMap.put("description", agent.getDescription());
                agentMap.put("prompt", agent.getPrompt());

                if (agent.getTools() != null) {
                    agentMap.put("tools", agent.getTools());
                }
                if (agent.getModel() != null) {
                    agentMap.put("model", agent.getModel());
                }

                agentsMap.put(entry.getKey(), agentMap);
            }

            return objectMapper.writeValueAsString(agentsMap);
        } catch (JsonProcessingException e) {
            throw new ClaudeSDKException("Failed to serialize agents", e);
        }
    }

    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    /**
     * Check Claude Code version and warn if below minimum required version.
     * This method runs with a 2-second timeout and silently continues if it fails.
     * Can be skipped by setting CLAUDE_AGENT_SDK_SKIP_VERSION_CHECK environment variable.
     */
    private void checkClaudeVersion(String cliPath) {
        // Check if version check should be skipped
        if (System.getenv("CLAUDE_AGENT_SDK_SKIP_VERSION_CHECK") != null) {
            return;
        }

        Process versionProcess = null;
        try {
            // Run 'claude -v' to get version
            ProcessBuilder pb = new ProcessBuilder(cliPath, "-v");
            versionProcess = pb.start();

            // Read output with timeout (2 seconds)
            BufferedReader versionReader = new BufferedReader(
                    new InputStreamReader(versionProcess.getInputStream()));

            // Wait for process with timeout
            boolean finished = versionProcess.waitFor(2, java.util.concurrent.TimeUnit.SECONDS);

            if (finished && versionProcess.exitValue() == 0) {
                String versionOutput = versionReader.readLine();
                if (versionOutput != null) {
                    versionOutput = versionOutput.trim();

                    // Extract version using regex: match pattern like "1.2.3"
                    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("([0-9]+\\.[0-9]+\\.[0-9]+)");
                    java.util.regex.Matcher matcher = pattern.matcher(versionOutput);

                    if (matcher.find()) {
                        String version = matcher.group(1);
                        String[] versionParts = version.split("\\.");
                        String[] minParts = MINIMUM_CLAUDE_CODE_VERSION.split("\\.");

                        // Compare versions (major.minor.patch)
                        boolean isOldVersion = false;
                        for (int i = 0; i < 3; i++) {
                            int currentPart = Integer.parseInt(versionParts[i]);
                            int minPart = Integer.parseInt(minParts[i]);

                            if (currentPart < minPart) {
                                isOldVersion = true;
                                break;
                            } else if (currentPart > minPart) {
                                break; // Version is newer
                            }
                        }

                        if (isOldVersion) {
                            String warning = String.format(
                                    "Warning: Claude Code version %s is unsupported in the Agent SDK. " +
                                    "Minimum required version is %s. " +
                                    "Some features may not work correctly.",
                                    version, MINIMUM_CLAUDE_CODE_VERSION
                            );
                            logger.warn(warning);
                            System.err.println(warning);
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Silently continue if version check fails
            logger.debug("Version check failed: {}", e.getMessage());
        } finally {
            if (versionProcess != null && versionProcess.isAlive()) {
                versionProcess.destroy();
                try {
                    versionProcess.waitFor(500, java.util.concurrent.TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
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
        // Clean up temporary files first
        for (Path tempFile : tempFiles) {
            try {
                Files.deleteIfExists(tempFile);
            } catch (IOException e) {
                logger.warn("Failed to delete temporary file: {}", tempFile, e);
            }
        }
        tempFiles.clear();

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
