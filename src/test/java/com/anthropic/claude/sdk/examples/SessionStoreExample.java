package com.anthropic.claude.sdk.examples;

import com.anthropic.claude.sdk.ClaudeAgentSdk;
import com.anthropic.claude.sdk.session.InMemorySessionStore;
import com.anthropic.claude.sdk.session.SessionInfo;
import com.anthropic.claude.sdk.session.SessionStore;
import com.anthropic.claude.sdk.types.messages.Message;
import com.anthropic.claude.sdk.types.options.ClaudeAgentOptions;
import com.anthropic.claude.sdk.types.options.PermissionMode;

import java.util.List;
import java.util.stream.Stream;

/**
 * Session Store Example - Demonstrates using SessionStore for session persistence.
 * <p>
 * This example shows how to:
 * 1. Create an in-memory session store
 * 2. Run a query with session storage enabled
 * 3. List stored sessions
 */
public class SessionStoreExample {
    public static void main(String[] args) {
        // Create an in-memory session store
        SessionStore store = new InMemorySessionStore();

        ClaudeAgentOptions options = ClaudeAgentOptions.builder()
                .sessionStore(store)
                .permissionMode(PermissionMode.ACCEPT_EDITS)
                .build();

        // Run a query - messages are mirrored to the store
        Stream<Message> messages = ClaudeAgentSdk.query("What is Java?", options);
        messages.forEach(msg -> {
            System.out.println("Message type: " + msg.getType());
        });

        // Load stored sessions
        List<SessionInfo> sessions = store.listSessions("default").join();
        for (SessionInfo session : sessions) {
            System.out.println("Session: " + session.sessionId() + " at " + session.mtime());
        }
    }
}
