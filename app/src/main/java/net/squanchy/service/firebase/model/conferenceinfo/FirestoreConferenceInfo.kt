package net.squanchy.service.firebase.model.conferenceinfo

import com.google.firebase.firestore.GeoPoint

class FirestoreVenue {

    lateinit var name: String
    lateinit var address: String
    lateinit var latLon: GeoPoint
    lateinit var description: String
    lateinit var mapUrl: String
    lateinit var timezone: String
}

class FirestoreConferenceInfo {

    lateinit var name: String
    lateinit var socialHashtag: String
    lateinit var twitterHandle: String
}
