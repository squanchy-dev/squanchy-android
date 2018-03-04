package net.squanchy.service.firebase.model

data class FirebaseSpeaker(
    var id: String? = null,
    var name: String? = null,
    var bio: String? = null,
    var company_name: String? = null,
    var company_url: String? = null,
    var personal_url: String? = null,
    var twitter_username: String? = null,
    var photo_url: String? = null
)
