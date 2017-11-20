package net.squanchy.google

enum class GoogleClientId {

    SIGN_IN_ACTIVITY;

    fun clientId(): Int = ordinal
}
