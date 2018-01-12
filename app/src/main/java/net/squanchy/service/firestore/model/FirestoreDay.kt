package net.squanchy.service.firestore.model

import java.util.Date

class FirestoreDay : WithId {
    var position: Long? = null
    lateinit var date: Date
    override lateinit var id: String
}
