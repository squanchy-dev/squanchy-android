package net.squanchy.service.firebase

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Process.THREAD_PRIORITY_BACKGROUND
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import net.squanchy.service.firebase.model.conferenceinfo.FirestoreConferenceInfo
import net.squanchy.service.firebase.model.conferenceinfo.FirestoreVenue
import net.squanchy.service.firebase.model.schedule.FirestoreEvent
import net.squanchy.service.firebase.model.schedule.FirestoreFavorite
import net.squanchy.service.firebase.model.schedule.FirestoreSchedulePage
import net.squanchy.service.firebase.model.schedule.FirestoreSpeaker
import net.squanchy.service.firebase.model.schedule.FirestoreTrack
import net.squanchy.service.firebase.model.twitter.FirestoreTweet
import org.joda.time.DateTimeZone
import java.util.concurrent.Executor
import java.util.concurrent.Semaphore

class FirestoreDbService(private val db: FirebaseFirestore) {

    private val looper: Looper by lazy { backgroundLooper() }

    fun scheduleView(): Observable<List<FirestoreSchedulePage>> {
        return view(VIEW_SCHEDULE)
            .collection(COLLECTION_SCHEDULE_PAGES)
            .orderBy(DAY_DATE_SORTING)
            .observe()
    }

    fun twitterView(): Observable<List<FirestoreTweet>> {
        return db.collection(SOCIAL_STREAM)
            .document(VIEW_TWITTER)
            .collection(COLLECTION_TWEETS)
            .observe()
    }

    fun timezone(): Observable<DateTimeZone> = venueInfo()
        .map { it.timezone }
        .map { DateTimeZone.forID(it) }

    fun venueInfo(): Observable<FirestoreVenue> {
        return db.collection(CONFERENCE_INFO)
            .document(VENUE)
            .observe()
    }

    fun conferenceInfo(): Observable<FirestoreConferenceInfo> {
        return db.collection(CONFERENCE_INFO)
            .document(CONFERENCE)
            .observe()
    }

    fun speakers(): Observable<List<FirestoreSpeaker>> {
        return view(VIEW_SPEAKERS)
            .collection(COLLECTION_SPEAKER_PAGES)
            .observe()
    }

    fun speaker(speakerId: String): Observable<FirestoreSpeaker> {
        return view(VIEW_SPEAKERS)
            .collection(COLLECTION_SPEAKER_PAGES)
            .document(speakerId)
            .observe()
    }

    fun events(): Observable<List<FirestoreEvent>> {
        return view(VIEW_EVENT_DETAILS)
            .collection(COLLECTION_EVENTS)
            .observe()
    }

    fun event(eventId: String): Observable<FirestoreEvent> {
        return view(VIEW_EVENT_DETAILS)
            .collection(COLLECTION_EVENTS)
            .document(eventId)
            .observe()
    }

    fun tracks(): Observable<List<FirestoreTrack>> {
        return view(VIEW_TRACKS)
            .collection(COLLECTION_TRACKS)
            .observe()
    }

    private fun view(viewName: String) = db.collection(VIEWS).document(viewName)

    fun favorites(userId: String): Observable<List<FirestoreFavorite>> {
        return db.collection(USER_DATA)
            .document(userId)
            .collection(COLLECTION_FAVORITES)
            .observe()
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

    private inline fun <reified T : Any> Query.observe(): Observable<List<T>> {
        return Observable.create<List<T>> { subscriber ->
            val registration = addSnapshotListener(
                HandlerExecutor(Handler()),
                EventListener<QuerySnapshot> { snapshot, exception ->
                    subscriber.emitSnapshotResult(snapshot, exception)
                }
            )
            subscriber.setCancellable { registration.remove() }
        }.subscribeOn(AndroidSchedulers.from(looper))
    }

    private inline fun <reified T : Any> DocumentReference.observe(): Observable<T> {
        return Observable.create<T> { subscriber ->
            val registration = addSnapshotListener(
                HandlerExecutor(Handler()),
                EventListener<DocumentSnapshot> { snapshot, exception ->
                    subscriber.emitSnapshotResult(snapshot, exception)
                }
            )
            subscriber.setCancellable { registration.remove() }
        }.subscribeOn(AndroidSchedulers.from(looper))
    }
}

private class HandlerExecutor(private val handler: Handler) : Executor {
    override fun execute(command: Runnable) {
        handler.post(command)
    }
}

fun backgroundLooper(): Looper {
    val semaphore = Semaphore(0)
    val handlerThread = object : HandlerThread("Firestore-Looper", THREAD_PRIORITY_BACKGROUND) {
        override fun onLooperPrepared() {
            semaphore.release()
        }
    }
    handlerThread.start()
    semaphore.acquireUninterruptibly()
    return handlerThread.looper
}

private inline fun <reified T : Any> ObservableEmitter<List<T>>.emitSnapshotResult(snapshot: QuerySnapshot?, exception: Exception?) {
    when {
        exception != null && isDisposed.not() -> onError(exception)
        else -> emitQueryResultValues(snapshot)
    }
}

@Suppress("TooGenericExceptionCaught") // Firestore doesn't have a strong Exception typing
private inline fun <reified T : Any> ObservableEmitter<List<T>>.emitQueryResultValues(snapshot: QuerySnapshot?) {
    try {
        val modelItems: List<T> = snapshot?.documents?.map { it.toObject(T::class.java) }
            ?.mapNotNull { it as T } ?: emptyList()

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
