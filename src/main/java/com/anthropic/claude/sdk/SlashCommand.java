package com.anthropic.claude.sdk;

/**
 * A slash command available in the CLI.
 */
public final class SlashCommand {

    private final String name;
    private final String description;
    private final String argumentHint;

    public SlashCommand(String name, String description, String argumentHint) {
        this.name = name;
        this.description = description;
        this.argumentHint = argumentHint;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public String argumentHint() {
        return argumentHint;
    }
}
