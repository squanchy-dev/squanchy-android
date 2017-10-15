package net.squanchy.google;

public enum GoogleClientId {

    HOME_ACTIVITY,
    SETTINGS_FRAGMENT,
    SIGN_IN_ACTIVITY;

    public int clientId() {
        return ordinal();
    }
}
