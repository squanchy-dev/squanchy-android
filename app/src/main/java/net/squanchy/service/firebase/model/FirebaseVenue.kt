package net.squanchy.service.firebase.model

data class FirebaseVenue(
        var name: String? = null,
        var address: String? = null,
        var lat: Double? = null,
        var lon: Double? = null,
        var description: String? = null,
        var map_url: String? = null,
        var timezone: String? = null
)
