package net.squanchy.service.firestore

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable
import io.reactivex.Observable
import net.squanchy.service.firestore.model.conferenceinfo.FirestoreConferenceInfo
import net.squanchy.service.firestore.model.conferenceinfo.FirestoreVenue
import net.squanchy.service.firestore.model.schedule.FirestoreEvent
import net.squanchy.service.firestore.model.schedule.FirestoreFavorite
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
            val registration = view(SCHEDULE)
                .collection(SCHEDULE_PAGES)
                .orderBy(DAY_DATE_SORTING)
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
            val registration = db.collection(SOCIAL_STREAM)
                .document(TWITTER)
                .collection(TWEETS)
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
            val registration = db.collection(CONFERENCE_INFO)
                .document(VENUE)
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
            val registration = db.collection(CONFERENCE_INFO)
                .document(CONFERENCE)
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
            val registration = view(SPEAKERS)
                .collection(SPEAKER_PAGES)
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
            val registration = view(SPEAKERS)
                .collection(SPEAKER_PAGES)
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
            val registration = view(EVENT_DETAILS)
                .collection(EVENTS)
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
            val registration = view(EVENT_DETAILS)
                .collection(EVENTS)
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

    fun tracks(): Observable<List<FirestoreTrack>> {
        return Observable.create { subscriber ->
            val registration = db.collection("tracks")
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null && subscriber.isDisposed.not()) {
                        subscriber.onError(exception)
                        return@addSnapshotListener
                    }
                    subscriber.onNext(snapshot.documents.map { trackSnapshot ->
                        trackSnapshot.toObject(FirestoreTrack::class.java).apply { id = trackSnapshot.id } // TODO should be done in the backend
                    })
                }

            subscriber.setCancellable { registration.remove() }
        }
    }

    private fun view(viewName: String) = db.collection(VIEWS).document(viewName)

    fun favorites(userId: String): Observable<List<FirestoreFavorite>> {
        return Observable.create { subscriber ->
            val registration = db.collection(USER_DATA)
                .document(userId)
                .collection(FAVORITES)
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null && subscriber.isDisposed.not()) {
                        subscriber.onError(exception)
                        return@addSnapshotListener
                    }
                    subscriber.onNext(snapshot.documents.map { favoriteSnapshot ->
                        favoriteSnapshot.toObject(FirestoreFavorite::class.java)
                    })
                }

            subscriber.setCancellable(registration::remove)
        }
    }

    fun addFavorite(eventId: String, userId: String): Completable =
        updateFavorite(eventId, userId, addToFirestoreAction)

    private val addToFirestoreAction: (DocumentReference.(String) -> Task<*>) = { eventId: String ->
        val firestoreFavorite = FirestoreFavorite().apply { id = eventId }
        collection(FAVORITES).document(eventId).set(firestoreFavorite)
    }

    fun removeFavorite(eventId: String, userId: String): Completable =
        updateFavorite(eventId, userId, deleteFromFirestoreAction)

    private val deleteFromFirestoreAction: (DocumentReference.(String) -> Task<*>) = { eventId ->
        collection(FAVORITES).document(eventId).delete()
    }

    private fun updateFavorite(eventId: String, userId: String, action: DocumentReference.(String) -> Task<*>): Completable {
        return Completable.create { emitter ->
            db.collection(USER_DATA)
                .document(userId)
                .action(eventId)
                .addOnFailureListener { exception ->
                    if (emitter.isDisposed.not()) {
                        emitter.onError(exception)
                    }
                }
                .addOnSuccessListener {
                    if (emitter.isDisposed.not()) {
                        emitter.onComplete()
                    }
                }
        }
    }

    companion object {
        private const val VIEWS = "views"
        private const val SPEAKERS = "speakers"
        private const val SPEAKER_PAGES = "speaker_pages"
        private const val CONFERENCE = "conference"
        private const val CONFERENCE_INFO = "conference_info"
        private const val VENUE = "venue"
        private const val SCHEDULE = "schedule"
        private const val SCHEDULE_PAGES = "schedule_pages"
        private const val EVENTS = "events"
        private const val EVENT_DETAILS = "event_details"
        private const val USER_DATA = "user_data"
        private const val FAVORITES = "favorites"
        private const val SOCIAL_STREAM = "social_stream"
        private const val TWITTER = "twitter"
        private const val TWEETS = "tweets"
        private const val DAY_DATE_SORTING = "day.date"
    }
}
