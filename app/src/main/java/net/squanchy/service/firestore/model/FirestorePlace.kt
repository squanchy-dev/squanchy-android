package net.squanchy.service.firestore.model

class FirestorePlace : WithId {
    override lateinit var id: String
    lateinit var name: String
    var floor: String? = null
}
