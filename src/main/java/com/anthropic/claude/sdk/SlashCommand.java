package com.anthropic.claude.sdk;

/**
 * A slash command available in the CLI.
 *
 * @param name         the command name (without the leading slash)
 * @param description  a brief description of what the command does
 * @param argumentHint hint text for the command's argument
 */
public record SlashCommand(
        String name,
        String description,
        String argumentHint
) {
}
