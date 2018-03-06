package net.squanchy.service.firebase.model

data class FirebaseUserData(
    var favorites: Map<String, Boolean>? = null,
    var achievements: Map<String, Long>? = null
)
