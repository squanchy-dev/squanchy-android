package net.squanchy.service.firestore.model.schedule

import java.util.Date

class FirestoreSchedulePage {
    lateinit var day: FirestoreDay
    var events: List<FirestoreEvent> = emptyList()
}

class FirestoreDay {
    lateinit var id: String
    lateinit var date: Date
}

class FirestoreEvent {
    lateinit var id: String
    lateinit var title: String
    lateinit var startTime: Date
    lateinit var endTime: Date
    var place: FirestorePlace? = null
    var track: FirestoreTrack? = null
    var speakers: List<FirestoreSpeaker> = emptyList()
    var experienceLevel: String? = null
    lateinit var type: String
    var description: String? = null
}

class FirestorePlace {
    lateinit var id: String
    lateinit var name: String
    var floor: String? = null
}

class FirestoreTrack {
    lateinit var id: String
    lateinit var name: String
    var accentColor: String? = null
    var textColor: String? = null
    var iconUrl: String? = null
}

class FirestoreSpeaker {
    lateinit var id: String
    lateinit var name: String
    lateinit var bio: String
    var companyName: String? = null
    var companyUrl: String? = null
    var personalUrl: String? = null
    var photoUrl: String? = null
    var twitterUsername: String? = null
}
