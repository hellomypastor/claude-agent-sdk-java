package com.anthropic.claude.examples;

import com.anthropic.claude.sdk.ClaudeAgent;
import com.anthropic.claude.sdk.errors.ClaudeSDKException;
import com.anthropic.claude.sdk.types.AssistantMessage;
import com.anthropic.claude.sdk.types.ContentBlock;
import com.anthropic.claude.sdk.types.Message;
import com.anthropic.claude.sdk.types.TextBlock;

import java.util.Iterator;

/**
 * Quick start example demonstrating basic query usage.
 */
public class QuickStartExample {

    public static void main(String[] args) {
        try {
            // Simple query
            Iterator<Message> messages = ClaudeAgent.query("What is 2 + 2?");

            // Process messages
            while (messages.hasNext()) {
                Message message = messages.next();
                if (message instanceof AssistantMessage) {
                    AssistantMessage assistantMsg = (AssistantMessage) message;
                    for (ContentBlock block : assistantMsg.getContent()) {
                        if (block instanceof TextBlock) {
                            TextBlock textBlock = (TextBlock) block;
                            System.out.println(textBlock.getText());
                        }
                    }
                }
            }

        } catch (ClaudeSDKException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
