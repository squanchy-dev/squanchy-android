package net.squanchy.google;

public enum GoogleClientId {

    HOME_ACTIVITY,
    LOCATION_ONBOARDING_ACTIVITY,
    SETTINGS_FRAGMENT,
    SIGN_IN_ACTIVITY;

    public int clientId() {
        return ordinal();
    }
}
