package net.squanchy.analytics

enum class LoginStatus(val rawLoginStatus: String) {
    NOT_LOGGED_IN("not_logged_in"),
    LOGGED_IN_ONBOARDING("logged_in_onboarding"),
    LOGGED_IN_SETTINGS("logged_in_settings"),
    LOGGED_IN_FAVORITES("logged_in_favorites"),
    LOGGED_IN_EVENT_DETAILS("logged_in_event_details")
}
