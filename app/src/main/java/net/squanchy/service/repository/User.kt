package net.squanchy.service.repository

data class User(
    val id: String,
    val isAnonymous: Boolean,
    val googleData: GoogleData?
)

data class GoogleData(
    val displayName: String,
    val photoUrl: String?
)
