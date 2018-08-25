package net.squanchy.service.firebase

import arrow.core.Option
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import io.reactivex.Completable
import io.reactivex.Observable
import net.squanchy.service.repository.AuthService
import net.squanchy.service.repository.User
import net.squanchy.support.lang.getOrThrow
import net.squanchy.support.lang.option

class FirebaseAuthService(private val auth: FirebaseAuth) : AuthService {

    override fun signInWithGoogle(account: GoogleSignInAccount): Completable {
        return currentFirebaseUser()
            .firstOrError()
            .flatMapCompletable {
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                return@flatMapCompletable if (it.isDefined()) {
                    linkAccountWithGoogleCredential(it.getOrThrow(), credential)
                } else {
                    signInWithGoogleCredential(credential)
                }
            }
    }

    private fun linkAccountWithGoogleCredential(user: FirebaseUser, credential: AuthCredential): Completable {
        return { user.linkWithCredential(credential) }.toCompletable()
            .onErrorResumeNext(deleteUserAndSignInWithCredentialIfLinkingFailed(user, credential))
    }

    private fun deleteUserAndSignInWithCredentialIfLinkingFailed(
        user: FirebaseUser,
        credential: AuthCredential
    ): (Throwable) -> Completable = { error ->
        val linkingFailed = linkingFailed(error)

        when {
            linkingFailed && user.isAnonymous -> deleteUser(user).andThen(signInWithGoogleCredential(credential))
            linkingFailed && !user.isAnonymous -> {
                Completable.error(IllegalStateException("Trying to link user with Google with non anonymous user", error))
            }
            else -> Completable.error(error)
        }
    }

    private fun linkingFailed(error: Throwable): Boolean {
        return error is FirebaseAuthUserCollisionException
    }

    private fun signInWithGoogleCredential(credential: AuthCredential): Completable {
        return { auth.signInWithCredential(credential) }.toCompletable()
    }

    private fun deleteUser(user: FirebaseUser): Completable {
        return { user.delete() }.toCompletable()
    }

    override fun <T> ifUserSignedInThenObservableFrom(observable: (String) -> Observable<T>): Observable<T> {
        return ifUserSignedIn()
            .switchMap { user -> observable(user.uid) }
    }

    override fun ifUserSignedInThenCompletableFrom(completable: (String) -> Completable): Completable {
        return ifUserSignedIn()
            .firstOrError()
            .flatMapCompletable { user -> completable(user.uid) }
    }

    private fun ifUserSignedIn(): Observable<FirebaseUser> {
        return currentFirebaseUser()
            .filter { it.isDefined() }
            .map { it.getOrThrow() }
    }

    private fun currentFirebaseUser(): Observable<Option<FirebaseUser>> {
        return Observable.create { emitter ->
            val listener = { firebaseAuth: FirebaseAuth ->
                try {
                    if (!emitter.isDisposed) {
                        emitter.onNext(firebaseAuth.currentUser.option())
                    }
                } catch (exception: FirebaseException) {
                    if (!emitter.isDisposed) {
                        emitter.onError(exception)
                    }
                }
            }
            auth.addAuthStateListener(listener)
            emitter.setCancellable { auth.removeAuthStateListener(listener) }
        }
    }

    override fun currentUser(): Observable<Option<User>> =
        currentFirebaseUser()
            .map { user -> user.map { it.toUser() } }

    override fun signOut(): Completable {
        auth.signOut()
        return signInAnonymously()
    }

    override fun signInAnonymously(): Completable {
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
