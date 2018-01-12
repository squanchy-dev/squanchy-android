package net.squanchy.service.firestore.model

import com.google.firebase.firestore.DocumentReference

class FirestoreSpeaker : WithId {
    override lateinit var id: String
    lateinit var user_profile: DocumentReference
    lateinit var bio: String
    var company_name: String? = null
    var company_url: String? = null
    var personal_url: String? = null
    var twitter_handle: String? = null


}

class FirestoreUser: WithId {
    override lateinit var id: String
    lateinit var full_name: String
    var profile_pic: String? = null
}
