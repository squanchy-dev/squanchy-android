package net.squanchy.proximity;

public class ProximityEvent {
    private String action;
    private String subject;

    public ProximityEvent(String action, String subject) {
        this.action = action;
        this.subject = subject;
    }

    public String getAction() {
        return action;
    }

    public String getSubject() {
        return subject;
    }
}
