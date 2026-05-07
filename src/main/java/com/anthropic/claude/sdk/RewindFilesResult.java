package com.anthropic.claude.sdk;

import java.util.List;

/**
 * Result of a file rewind operation.
 */
public final class RewindFilesResult {

    private final boolean canRewind;
    private final String error;
    private final List<String> filesChanged;
    private final Integer insertions;
    private final Integer deletions;

    public RewindFilesResult(boolean canRewind, String error, List<String> filesChanged,
                             Integer insertions, Integer deletions) {
        this.canRewind = canRewind;
        this.error = error;
        this.filesChanged = filesChanged;
        this.insertions = insertions;
        this.deletions = deletions;
    }

    public boolean canRewind() {
        return canRewind;
    }

    public String error() {
        return error;
    }

    public List<String> filesChanged() {
        return filesChanged;
    }

    public Integer insertions() {
        return insertions;
    }

    public Integer deletions() {
        return deletions;
    }
}
