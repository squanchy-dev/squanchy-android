package net.squanchy.service.firestore.model

import com.google.firebase.firestore.DocumentReference
import java.util.Date

class FirestoreEvent : WithId {
    override lateinit var id: String
    lateinit var day: DocumentReference
    lateinit var start_time: Date
    lateinit var end_time: Date
    var place: DocumentReference? = null
    lateinit var submission: DocumentReference
    var track: DocumentReference? = null
    lateinit var type: String
}
