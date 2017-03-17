package net.squanchy.analytics;

public enum ContentType {
    NAVIGATION_ITEM("navigation bar item"),
    SCHEDULE_DAY("schedule day"),
    SCHEDULE_ITEM("schedule item"),
    FAVORITES_ITEM("favorites item");

    private final String rawContentType;

    ContentType(String rawContentType) {
        this.rawContentType = rawContentType;
    }

    public String rawContentType() {
        return rawContentType;
    }
}
