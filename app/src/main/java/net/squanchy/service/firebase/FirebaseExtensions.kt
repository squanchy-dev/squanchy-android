package net.squanchy.service.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import io.reactivex.Completable

fun <T> (() -> Task<T>).toCompletable(): Completable {
    return Completable.create { completableObserver ->
        invoke().addOnCompleteListener { result ->
            if (result.isSuccessful && !completableObserver.isDisposed) {
                completableObserver.onComplete()
            } else {
                completableObserver.onError(result.exception
                    ?: FirebaseException("Unknown exception in Firebase Auth")
                )
            }
        }
    }
}
