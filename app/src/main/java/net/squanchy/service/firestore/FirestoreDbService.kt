package net.squanchy.service.firestore

import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Observable
import net.squanchy.service.firestore.model.conferenceinfo.FirestoreConferenceInfo
import net.squanchy.service.firestore.model.conferenceinfo.FirestoreVenue
import net.squanchy.service.firestore.model.schedule.FirestoreEvent
import net.squanchy.service.firestore.model.schedule.FirestoreSchedulePage
import net.squanchy.service.firestore.model.schedule.FirestoreSpeaker
import net.squanchy.service.firestore.model.schedule.FirestoreTrack
import net.squanchy.service.firestore.model.twitter.FirestoreTweet
import org.joda.time.DateTimeZone

// TODO
// val onCurrentThread = Executors.newSingleThreadExecutor { Thread.currentThread() }
class FirestoreDbService(private val db: FirebaseFirestore) {

    fun scheduleView(): Observable<List<FirestoreSchedulePage>> {
        return Observable.create { subscriber ->
            val registration = db.collection("views")
                .document("schedule")
                .collection("schedule_pages")
                .orderBy("day.date")
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        subscriber.onError(exception)
                        return@addSnapshotListener
                    }

                    subscriber.onNext(snapshot.documents.map { it.toObject(FirestoreSchedulePage::class.java) })
                }

            subscriber.setCancellable { registration.remove() }
        }
    }

    fun twitterView(): Observable<List<FirestoreTweet>> {
        return Observable.create { subscriber ->
            val registration = db.collection("social_stream")
                .document("twitter")
                .collection("tweets")
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null && subscriber.isDisposed.not()) {
                        subscriber.onError(exception)
                        return@addSnapshotListener
                    }
                    subscriber.onNext(snapshot.documents.map { it.toObject(FirestoreTweet::class.java) })
                }

            subscriber.setCancellable { registration.remove() }
        }
    }

    fun timezone(): Observable<DateTimeZone> = venueInfo()
        .map { it.timezone }
        .map { DateTimeZone.forID(it) }

    fun venueInfo(): Observable<FirestoreVenue> {
        return Observable.create { subscriber ->
            val registration = db.collection("conference_info")
                .document("venue")
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null && subscriber.isDisposed.not()) {
                        subscriber.onError(exception)
                        return@addSnapshotListener
                    }
                    subscriber.onNext(snapshot.toObject(FirestoreVenue::class.java))
                }

            subscriber.setCancellable { registration.remove() }
        }
    }

    fun conferenceInfo(): Observable<FirestoreConferenceInfo> {
        return Observable.create { subscriber ->
            val registration = db.collection("conference_info")
                .document("conference")
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null && subscriber.isDisposed.not()) {
                        subscriber.onError(exception)
                        return@addSnapshotListener
                    }
                    subscriber.onNext(snapshot.toObject(FirestoreConferenceInfo::class.java))
                }

            subscriber.setCancellable { registration.remove() }
        }
    }

    fun speakers(): Observable<List<FirestoreSpeaker>> {
        return Observable.create { subscriber ->
            val registration = db.collection("views")
                .document("speakers")
                .collection("speaker_pages")
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null && subscriber.isDisposed.not()) {
                        subscriber.onError(exception)
                        return@addSnapshotListener
                    }
                    subscriber.onNext(snapshot.documents.map { it.toObject(FirestoreSpeaker::class.java) })
                }

            subscriber.setCancellable { registration.remove() }
        }
    }

    fun speaker(speakerId: String): Observable<FirestoreSpeaker> {
        return Observable.create { subscriber ->
            val registration = db.collection("views")
                .document("speakers")
                .collection("speaker_pages")
                .document(speakerId)
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null && subscriber.isDisposed.not()) {
                        subscriber.onError(exception)
                        return@addSnapshotListener
                    }
                    subscriber.onNext(snapshot.toObject(FirestoreSpeaker::class.java))
                }

            subscriber.setCancellable { registration.remove() }
        }
    }

    fun events(): Observable<List<FirestoreEvent>> {
        return Observable.create { subscriber ->
            val registration = db.collection("views")
                .document("event_details")
                .collection("events")
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null && subscriber.isDisposed.not()) {
                        subscriber.onError(exception)
                        return@addSnapshotListener
                    }
                    subscriber.onNext(snapshot.documents.map { it.toObject(FirestoreEvent::class.java) })
                }

            subscriber.setCancellable { registration.remove() }
        }
    }

    fun event(eventId: String): Observable<FirestoreEvent> {
        return Observable.create { subscriber ->
            val registration = db.collection("views")
                .document("event_details")
                .collection("events")
                .document(eventId)
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null && subscriber.isDisposed.not()) {
                        subscriber.onError(exception)
                        return@addSnapshotListener
                    }
                    subscriber.onNext(snapshot.toObject(FirestoreEvent::class.java))
                }

            subscriber.setCancellable { registration.remove() }
        }
    }
}
