package net.squanchy.service.firestore

import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Observable
import net.squanchy.service.firestore.model.conferenceinfo.FirestoreVenue
import net.squanchy.service.firestore.model.schedule.FirestoreSchedulePage
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
                    subscriber.onNext(snapshot.toObject(FirestoreVenue::class.java) )
                }

            subscriber.setCancellable { registration.remove() }
        }
    }
}
