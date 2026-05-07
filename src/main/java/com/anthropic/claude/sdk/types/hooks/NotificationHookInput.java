package com.anthropic.claude.sdk.types.hooks;

/**
 * Hook input for the Notification event.
 * Fired when a notification is triggered during the session.
 */
public final class NotificationHookInput extends BaseHookInput implements HookInput {

    private final String message;
    private final String title;
    private final String notificationType;

    public NotificationHookInput(String sessionId, String transcriptPath, String cwd, String permissionMode,
                                 String message, String title, String notificationType) {
        super(sessionId, transcriptPath, cwd, permissionMode);
        this.message = message;
        this.title = title;
        this.notificationType = notificationType;
    }

    public String message() {
        return message;
    }

    public String title() {
        return title;
    }

    public String notificationType() {
        return notificationType;
    }

    @Override
    public String hookEventName() {
        return "Notification";
    }
}
