package net.squanchy.service.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import net.squanchy.service.firebase.model.conferenceinfo.FirestoreConferenceInfo
import net.squanchy.service.firebase.model.conferenceinfo.FirestoreVenue
import net.squanchy.service.firebase.model.schedule.FirestoreEvent
import net.squanchy.service.firebase.model.schedule.FirestoreFavorite
import net.squanchy.service.firebase.model.schedule.FirestoreSchedulePage
import net.squanchy.service.firebase.model.schedule.FirestoreSpeaker
import net.squanchy.service.firebase.model.schedule.FirestoreTrack
import net.squanchy.service.firebase.model.twitter.FirestoreTweet
import org.joda.time.DateTimeZone

class FirestoreDbService(private val db: FirebaseFirestore) {

    fun scheduleView(): Observable<List<FirestoreSchedulePage>> {
        return Observable.create { subscriber ->
            val registration = view(VIEW_SCHEDULE)
                .collection(COLLECTION_SCHEDULE_PAGES)
                .orderBy(DAY_DATE_SORTING)
                .addSnapshotListener { snapshot, exception -> subscriber.emitSnapshotResult(snapshot, exception) }

            subscriber.setCancellable { registration.remove() }
        }
    }

    fun twitterView(): Observable<List<FirestoreTweet>> {
        return Observable.create { subscriber ->
            val registration = db.collection(SOCIAL_STREAM)
                .document(VIEW_TWITTER)
                .collection(COLLECTION_TWEETS)
                .addSnapshotListener { snapshot, exception -> subscriber.emitSnapshotResult(snapshot, exception) }

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
                .addSnapshotListener { snapshot, exception -> subscriber.emitSnapshotResult(snapshot, exception) }

            subscriber.setCancellable { registration.remove() }
        }
    }

    fun conferenceInfo(): Observable<FirestoreConferenceInfo> {
        return Observable.create { subscriber ->
            val registration = db.collection(CONFERENCE_INFO)
                .document(CONFERENCE)
                .addSnapshotListener { snapshot, exception -> subscriber.emitSnapshotResult(snapshot, exception) }

            subscriber.setCancellable { registration.remove() }
        }
    }

    fun speakers(): Observable<List<FirestoreSpeaker>> {
        return Observable.create { subscriber ->
            val registration = view(VIEW_SPEAKERS)
                .collection(COLLECTION_SPEAKER_PAGES)
                .addSnapshotListener { snapshot, exception -> subscriber.emitSnapshotResult(snapshot, exception) }

            subscriber.setCancellable { registration.remove() }
        }
    }

    fun speaker(speakerId: String): Observable<FirestoreSpeaker> {
        return Observable.create { subscriber ->
            val registration = view(VIEW_SPEAKERS)
                .collection(COLLECTION_SPEAKER_PAGES)
                .document(speakerId)
                .addSnapshotListener { snapshot, exception -> subscriber.emitSnapshotResult(snapshot, exception) }

            subscriber.setCancellable { registration.remove() }
        }
    }

    fun events(): Observable<List<FirestoreEvent>> {
        return Observable.create { subscriber ->
            val registration = view(VIEW_EVENT_DETAILS)
                .collection(COLLECTION_EVENTS)
                .addSnapshotListener { snapshot, exception -> subscriber.emitSnapshotResult(snapshot, exception) }

            subscriber.setCancellable { registration.remove() }
        }
    }

    fun event(eventId: String): Observable<FirestoreEvent> {
        return Observable.create { subscriber ->
            val registration = view(VIEW_EVENT_DETAILS)
                .collection(COLLECTION_EVENTS)
                .document(eventId)
                .addSnapshotListener { snapshot, exception -> subscriber.emitSnapshotResult(snapshot, exception) }

            subscriber.setCancellable { registration.remove() }
        }
    }

    fun tracks(): Observable<List<FirestoreTrack>> {
        return Observable.create { subscriber ->
            val registration = view(VIEW_TRACKS)
                .collection(COLLECTION_TRACKS)
                .addSnapshotListener { snapshot, exception -> subscriber.emitSnapshotResult(snapshot, exception) }

            subscriber.setCancellable { registration.remove() }
        }
    }

    private fun view(viewName: String) = db.collection(VIEWS).document(viewName)

    fun favorites(userId: String): Observable<List<FirestoreFavorite>> {
        return Observable.create { subscriber ->
            val registration = db.collection(USER_DATA)
                .document(userId)
                .collection(COLLECTION_FAVORITES)
                .addSnapshotListener { snapshot, exception -> subscriber.emitSnapshotResult(snapshot, exception) }

            subscriber.setCancellable(registration::remove)
        }
    }

    fun addFavorite(eventId: String, userId: String): Completable =
        updateFavorite(eventId, userId, addToFirestoreAction)

    private val addToFirestoreAction: (DocumentReference.(String) -> Task<*>) = { eventId: String ->
        val firestoreFavorite = FirestoreFavorite().apply { id = eventId }
        collection(COLLECTION_FAVORITES).document(eventId).set(firestoreFavorite)
    }

    fun removeFavorite(eventId: String, userId: String): Completable =
        updateFavorite(eventId, userId, deleteFromFirestoreAction)

    private val deleteFromFirestoreAction: (DocumentReference.(String) -> Task<*>) = { eventId ->
        collection(COLLECTION_FAVORITES).document(eventId).delete()
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
        private const val CONFERENCE = "conference"
        private const val CONFERENCE_INFO = "conference_info"
        private const val VENUE = "venue"
        private const val SOCIAL_STREAM = "social_stream"
        private const val USER_DATA = "user_data"

        private const val VIEW_TWITTER = "twitter"
        private const val COLLECTION_TWEETS = "tweets"
        private const val VIEW_SPEAKERS = "speakers"
        private const val COLLECTION_SPEAKER_PAGES = "speaker_pages"
        private const val VIEW_SCHEDULE = "schedule"
        private const val COLLECTION_SCHEDULE_PAGES = "schedule_pages"
        private const val VIEW_EVENT_DETAILS = "event_details"
        private const val COLLECTION_EVENTS = "events"
        private const val COLLECTION_FAVORITES = "favorites"
        private const val VIEW_TRACKS = "tracks"
        private const val COLLECTION_TRACKS = "tracks"

        private const val DAY_DATE_SORTING = "day.date"
    }
}

private inline fun <reified T> ObservableEmitter<List<T>>.emitSnapshotResult(snapshot: QuerySnapshot?, exception: Exception?) {
    when {
        exception != null && isDisposed.not() -> onError(exception)
        else -> emitQueryResultValues(snapshot)
    }
}

@Suppress("TooGenericExceptionCaught") // Firestore doesn't have a strong Exception typing
private inline fun <reified T> ObservableEmitter<List<T>>.emitQueryResultValues(snapshot: QuerySnapshot?) {
    try {
        val modelItems = snapshot?.documents?.map { it.toObject(T::class.java) }
            ?.filter { it != null }
            ?: emptyList()

        onNext(modelItems)
    } catch (e: RuntimeException) {
        onError(e)
    }
}

private inline fun <reified T> ObservableEmitter<T>.emitSnapshotResult(snapshot: DocumentSnapshot?, exception: Exception?) {
    when {
        exception != null && isDisposed.not() -> onError(exception)
        snapshot?.exists() == true -> emitExistingSnapshotValue(snapshot)
        else -> onError(NonExistingDocumentException(snapshot))
    }
}

@Suppress("TooGenericExceptionCaught") // Firestore doesn't have a strong Exception typing
private inline fun <reified T> ObservableEmitter<T>.emitExistingSnapshotValue(snapshot: DocumentSnapshot) {
    try {
        val modelObject = snapshot.toObject(T::class.java)

        if (modelObject == null) {
            onError(NonExistingDocumentException(snapshot))
        } else {
            onNext(modelObject)
        }
    } catch (e: RuntimeException) {
        onError(e)
    }
}

private class NonExistingDocumentException(snapshot: DocumentSnapshot?) : RuntimeException(
    "The specified path does not exist on the DB: ${snapshot?.reference?.path ?: "[null snapshot]"}"
)
