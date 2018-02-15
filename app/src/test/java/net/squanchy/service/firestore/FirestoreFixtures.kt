package net.squanchy.service.firestore

import android.text.format.DateUtils
import com.google.firebase.firestore.GeoPoint
import net.squanchy.service.firestore.model.conferenceinfo.FirestoreConferenceInfo
import net.squanchy.service.firestore.model.conferenceinfo.FirestoreVenue
import net.squanchy.service.firestore.model.schedule.FirestoreDay
import net.squanchy.service.firestore.model.schedule.FirestoreEvent
import net.squanchy.service.firestore.model.schedule.FirestorePlace
import net.squanchy.service.firestore.model.schedule.FirestoreSchedulePage
import net.squanchy.service.firestore.model.schedule.FirestoreSpeaker
import net.squanchy.service.firestore.model.schedule.FirestoreTrack
import java.util.Date

private const val MILLIS_OF_A_DATE_1 = 1518652975855
private const val MILLIS_OF_A_DATE_2 = MILLIS_OF_A_DATE_1 + DateUtils.DAY_IN_MILLIS

fun aFirestoreTrack(
    id: String = "fakeId",
    name: String = "fakeTrack",
    accentColor: String? = "#ABCDEF",
    textColor: String? = "#FEDCBA",
    iconUrl: String? = "www.squanchy.net"
) = FirestoreTrack().apply {
    this.id = id
    this.name = name
    this.accentColor = accentColor
    this.textColor = textColor
    this.iconUrl = iconUrl
}

fun aFirestoreSpeaker(
    id: String = "speakerId",
    name: String = "Boaty McBoatface",
    bio: String = "Hello, I'm a fake",
    companyName: String? = "Squanchy",
    companyUrl: String? = "www.squanchy.net",
    personalUrl: String? = "www.squanchy.net",
    photoUrl: String? = "@DonuldTrump",
    twitterUsername: String? = null
) = FirestoreSpeaker().apply {
    this.id = id
    this.name = name
    this.bio = bio
    this.companyName = companyName
    this.companyUrl = companyUrl
    this.personalUrl = personalUrl
    this.photoUrl = photoUrl
    this.twitterUsername = twitterUsername
}

fun aFirestorePlace(
    id: String = "placeId",
    name: String = "Turin",
    floor: String? = "This floor"
) = FirestorePlace().apply {
    this.id = id
    this.name = name
    this.floor = floor
}

fun aFirestoreDay(
    id: String = "dayId",
    date: Date = Date(MILLIS_OF_A_DATE_1)
) = FirestoreDay().apply {
    this.id = id
    this.date = date
}

fun aFirestoreVenue(
    name: String = "Venue Of The Conference",
    address: String = "Conference rd",
    latLon: GeoPoint = GeoPoint(0.0, 0.0),
    description: String = "This is the conference you're looking for",
    mapUrl: String = "www.maps.google.com/conference",
    timezone: String = "Europe/Rome"
) = FirestoreVenue().apply {
    this.name = name
    this.address = address
    this.latLon = latLon
    this.description = description
    this.mapUrl = mapUrl
    this.timezone = timezone
}

fun aFirestoreEvent(
    id: String = "EventId",
    title: String = "Getting started with fakes",
    startTime: Date = Date(MILLIS_OF_A_DATE_1),
    endTime: Date = Date(MILLIS_OF_A_DATE_2),
    place: FirestorePlace? = aFirestorePlace(),
    track: FirestoreTrack? = aFirestoreTrack(),
    speakers: List<FirestoreSpeaker> = listOf(aFirestoreSpeaker()),
    experienceLevel: String? = "beginner",
    type: String = "keynote",
    description: String? = "Something something an event"
) = FirestoreEvent().apply {
    this.id = id
    this.title = title
    this.startTime = startTime
    this.endTime = endTime
    this.place = place
    this.track = track
    this.type = type
    this.speakers = speakers
    this.description = description
    this.experienceLevel = experienceLevel
}

fun aFirestoreSchedulePage(
    day: FirestoreDay = aFirestoreDay(),
    events: List<FirestoreEvent> = (0..3).map { aFirestoreEvent() }
) = FirestoreSchedulePage().apply {
    this.day = day
    this.events = events
}

fun aFirestoreConferenceInfo(
    name: String = "Android Conference",
    socialHashtag: String = "#AndroidIsCool",
    twitterHandle: String = "@ThisIsAFakeConf"
) = FirestoreConferenceInfo().apply {
    this.name = name
    this.socialHashtag = socialHashtag
    this.twitterHandle = twitterHandle
}
