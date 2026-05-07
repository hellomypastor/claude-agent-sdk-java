package com.anthropic.claude.sdk;

import java.util.List;

/**
 * Result of a file rewind operation.
 *
 * @param canRewind    whether the rewind can be performed
 * @param error        error message if the rewind cannot be performed
 * @param filesChanged list of files that were changed
 * @param insertions   number of line insertions
 * @param deletions    number of line deletions
 */
public record RewindFilesResult(
        boolean canRewind,
        String error,
        List<String> filesChanged,
        Integer insertions,
        Integer deletions
) {
}
