package net.squanchy.analytics

enum class ContentType(val rawContentType: String) {
    NAVIGATION_ITEM("navigation bar item"),
    SCHEDULE_DAY("schedule day"),
    SCHEDULE_ITEM("schedule item"),
    FAVORITES_ITEM("favorites item");
}
