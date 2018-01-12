package net.squanchy.service.firestore

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import io.reactivex.Observable
import net.squanchy.service.firestore.model.FirestoreDay
import net.squanchy.service.firestore.model.FirestoreEvent
import net.squanchy.service.firestore.model.toObjectWithId

// TODO
// val onCurrentThread = Executors.newSingleThreadExecutor { Thread.currentThread() }
class FirestoreDbService(private val db: FirebaseFirestore) {
    fun days(): Observable<List<FirestoreDay>> {
        return Observable.create<List<FirestoreDay>> { subscriber ->
            db.collection("days").get()
                .addOnSuccessListener(
                        OnSuccessListener<QuerySnapshot> {
                            subscriber.onNext(
                                    it.map {
                                        it.toObjectWithId(FirestoreDay::class.java)
                                    }
                            )
                        }
                )
                .addOnFailureListener(OnFailureListener { error -> subscriber.onError(error) })
        }
    }

    fun event(eventId: String): Observable<FirestoreEvent> {
        return Observable.create { subscriber ->
            db.collection("events").document(eventId).get()
                .addOnSuccessListener { subscriber.onNext(it.toObjectWithId(FirestoreEvent::class.java)) }
                .addOnFailureListener { subscriber.onError(it) }
        }

    }
}

