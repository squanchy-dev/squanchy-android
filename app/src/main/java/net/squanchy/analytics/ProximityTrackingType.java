package net.squanchy.analytics;

import it.near.sdk.recipes.models.Recipe;

public enum ProximityTrackingType {
    NOTIFIED(Recipe.NOTIFIED_STATUS),
    ENGAGED(Recipe.ENGAGED_STATUS);

    private final String rawTrackingType;

    ProximityTrackingType(String rawTrackingType) {
        this.rawTrackingType = rawTrackingType;
    }

    public String rawTrackingType() {
        return rawTrackingType;
    }
}
