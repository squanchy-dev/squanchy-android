package net.squanchy.service.firestore.model

class FirestoreTrack : WithId {
    override lateinit var id: String
    lateinit var name: String
    var accent_color: String? = null
    var text_color: String? = null
    var icon_url: String? = null
}
