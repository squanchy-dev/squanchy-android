package net.squanchy.service

import io.reactivex.Observable
import net.squanchy.service.firestore.model.conferenceinfo.FirestoreConferenceInfo
import net.squanchy.service.firestore.model.conferenceinfo.FirestoreVenue
import net.squanchy.service.firestore.model.schedule.FirestoreEvent
import net.squanchy.service.firestore.model.schedule.FirestoreSchedulePage
import net.squanchy.service.firestore.model.schedule.FirestoreSpeaker
import net.squanchy.service.firestore.model.twitter.FirestoreTweet
import org.joda.time.DateTimeZone

interface DbService {
    fun scheduleView(): Observable<List<FirestoreSchedulePage>>
    fun twitterView(): Observable<List<FirestoreTweet>>
    fun timezone(): Observable<DateTimeZone>
    fun venueInfo(): Observable<FirestoreVenue>
    fun conferenceInfo(): Observable<FirestoreConferenceInfo>
    fun speakers(): Observable<List<FirestoreSpeaker>>
    fun speaker(speakerId: String): Observable<FirestoreSpeaker>
    fun events(): Observable<List<FirestoreEvent>>
    fun event(eventId: String): Observable<FirestoreEvent>
}
