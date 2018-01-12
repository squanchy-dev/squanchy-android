package net.squanchy.service.firestore.model

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import io.reactivex.Observable
import net.squanchy.support.lang.Optional

interface WithId {
    var id: String
}

fun <T : WithId> DocumentSnapshot.toObjectWithId(clazz: Class<T>)
        = let { toObject(clazz).apply { id = it.id } }

fun <T : WithId> DocumentReference.getWithId(clazz: Class<T>): Observable<T>
        = Observable.create { subscriber ->
    this.get()
        .addOnSuccessListener { subscriber.onNext(it.toObjectWithId(clazz)) }
        .addOnFailureListener { subscriber.onError(it) }
}


fun <T : WithId> DocumentReference?.getOptionalWithId(clazz: Class<T>): Observable<Optional<T>> {
    if (this == null) {
        return Observable.just(Optional.absent())
    }

    return Observable.create { subscriber ->
        this.get()
            .addOnSuccessListener { subscriber.onNext(Optional.of(it.toObjectWithId(clazz))) }
            .addOnFailureListener { subscriber.onError(it) }
    }
}
