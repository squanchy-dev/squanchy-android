package net.squanchy.google;

public enum GoogleClientId {

    SIGN_IN_ACTIVITY;

    public int clientId() {
        return ordinal();
    }
}
