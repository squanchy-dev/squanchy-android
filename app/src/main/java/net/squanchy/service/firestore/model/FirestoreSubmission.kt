package net.squanchy.service.firestore.model

import com.google.firebase.firestore.DocumentReference

class FirestoreSubmission : WithId {
    override lateinit var id: String
    lateinit var title: String
    var abstract: String? = null
    lateinit var category: DocumentReference
    lateinit var level: DocumentReference
    lateinit var speakers: List<DocumentReference> // todo check if this breaks
}
