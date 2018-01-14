package net.squanchy.service.firestore

import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Observable
import net.squanchy.service.firestore.model.schedule.FirestoreSchedulePage

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
}

