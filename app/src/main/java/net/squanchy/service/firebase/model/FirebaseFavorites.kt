package net.squanchy.service.firebase.model

data class FirebaseFavorites(var favorites: Map<String, Boolean> = emptyMap()) {
    fun hasFavorite(eventId: String) = favorites.containsKey(eventId)
}
