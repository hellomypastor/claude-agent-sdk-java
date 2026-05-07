package com.anthropic.claude.sdk;

/**
 * Account information returned by the CLI.
 */
public final class AccountInfo {

    private final String email;
    private final String organization;
    private final String subscriptionType;
    private final String tokenSource;
    private final String apiKeySource;

    public AccountInfo(String email, String organization, String subscriptionType,
                       String tokenSource, String apiKeySource) {
        this.email = email;
        this.organization = organization;
        this.subscriptionType = subscriptionType;
        this.tokenSource = tokenSource;
        this.apiKeySource = apiKeySource;
    }

    public String email() {
        return email;
    }

    public String organization() {
        return organization;
    }

    public String subscriptionType() {
        return subscriptionType;
    }

    public String tokenSource() {
        return tokenSource;
    }

    public String apiKeySource() {
        return apiKeySource;
    }
}
