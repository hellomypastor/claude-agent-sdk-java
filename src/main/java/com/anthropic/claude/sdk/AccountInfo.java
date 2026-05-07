package com.anthropic.claude.sdk;

/**
 * Account information returned by the CLI.
 *
 * @param email            the account email address
 * @param organization     the organization name
 * @param subscriptionType the subscription type (e.g. "pro", "enterprise")
 * @param tokenSource      how the auth token was obtained
 * @param apiKeySource     how the API key was obtained
 */
public record AccountInfo(
        String email,
        String organization,
        String subscriptionType,
        String tokenSource,
        String apiKeySource
) {
}
