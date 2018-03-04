package net.squanchy.service.firebase.model

data class FirebaseEvent(
    var id: String? = null,
    var name: String? = null,
    var type: String? = null,
    var start_time: Long? = null,
    var end_time: Long? = null,
    var day_id: String? = null,
    var description: String? = null,
    var track_id: String? = null,
    var experience_level: String? = null,
    var place_id: String? = null,
    var speaker_ids: List<String>? = null,
    var links: FirebaseLinks? = null
)
