package net.squanchy.service.firebase

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import io.reactivex.Completable
import io.reactivex.CompletableSource
import io.reactivex.Observable
import net.squanchy.service.repository.User
import net.squanchy.support.lang.Optional

class FirebaseAuthService(private val auth: FirebaseAuth) {

    fun signInWithGoogle(account: GoogleSignInAccount): Completable {
        return currentFirebaseUser()
            .firstOrError()
            .flatMapCompletable {
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                if (it.isPresent) {
                    linkAccountWithGoogleCredential(it.get(), credential)
                } else {
                    signInWithGoogleCredential(credential)
                }
            }
    }

    private fun linkAccountWithGoogleCredential(user: FirebaseUser, credential: AuthCredential): Completable {
        return fromTask(lazy { user.linkWithCredential(credential) })
            .onErrorResumeNext(deleteUserAndSignInWithCredentialIfLinkingFailed(user, credential))
    }

    private fun deleteUserAndSignInWithCredentialIfLinkingFailed(user: FirebaseUser, credential: AuthCredential): (Throwable) -> CompletableSource =
        {
            if (!linkingFailed(it)) {
                Completable.error(it)
            } else {
                if (!user.isAnonymous) {
                    Completable.error(IllegalStateException("Trying to link user with Google with non anonymous user", it))
                }

                deleteUser(user).andThen(signInWithGoogleCredential(credential))
            }
        }

    private fun linkingFailed(error: Throwable): Boolean {
        return error is FirebaseAuthUserCollisionException
    }

    private fun signInWithGoogleCredential(credential: AuthCredential): Completable {
        return fromTask(lazy { auth.signInWithCredential(credential) })
    }

    private fun deleteUser(user: FirebaseUser): Completable {
        return fromTask(lazy { user.delete() })
    }

    private fun <T> fromTask(task: Lazy<Task<T>>): Completable {
        return Completable.create { completableObserver ->
            task.value.addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    completableObserver.onComplete()
                } else {
                    completableObserver.onError(
                        result.exception
                            ?: FirebaseException("Unknown exception in Firebase Auth")
                    )
                }
            }
        }
    }

    fun <T> ifUserSignedInThenObservableFrom(observable: (String) -> Observable<T>): Observable<T> {
        return ifUserSignedIn()
            .switchMap { user -> observable(user.uid) }
    }

    fun ifUserSignedInThenCompletableFrom(completable: (String) -> Completable): Completable {
        return ifUserSignedIn()
            .firstOrError()
            .flatMapCompletable { user -> completable(user.uid) }
    }

    private fun ifUserSignedIn(): Observable<FirebaseUser> {
        return currentFirebaseUser()
            .filter({ it.isPresent })
            .map { it.get() }
    }

    private fun currentFirebaseUser(): Observable<Optional<FirebaseUser>> {
        return Observable.create { e ->
            val listener = { firebaseAuth: FirebaseAuth -> e.onNext(Optional.fromNullable(firebaseAuth.currentUser)) }

            auth.addAuthStateListener(listener)

            e.setCancellable { auth.removeAuthStateListener(listener) }
        }
    }

    fun currentUser(): Observable<Optional<User>> {
        return currentFirebaseUser().map { it.map { it.toUser() } }
    }

    private fun FirebaseUser.toUser() = User(uid, isAnonymous, providerData[0].photoUrl)

    fun signOut(): Completable {
        auth.signOut()
        return signInAnonymously()
    }

    fun signInAnonymously(): Completable {
        return Completable.create { emitter ->
            auth.signInAnonymously()
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { e ->
                    if (emitter.isDisposed) {
                        return@addOnFailureListener
                    }
                    emitter.onError(e)
                }
        }
    }
}
