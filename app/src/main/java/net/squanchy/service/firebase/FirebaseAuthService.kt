package net.squanchy.service.firebase

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import io.reactivex.Completable
import io.reactivex.CompletableSource
import io.reactivex.Observable
import net.squanchy.service.repository.AuthService
import net.squanchy.service.repository.User
import net.squanchy.support.lang.Optional

class FirebaseAuthService(private val auth: FirebaseAuth) : AuthService {

    override fun signInWithGoogle(account: GoogleSignInAccount): Completable {
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
        return { user.linkWithCredential(credential) }.toCompletable()
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
            .filter { it.isPresent }
            .map { it.get() }
    }

    private fun currentFirebaseUser(): Observable<Optional<FirebaseUser>> {
        return Observable.create { e ->
            val listener = { firebaseAuth: FirebaseAuth -> e.onNext(Optional.fromNullable(firebaseAuth.currentUser)) }

            auth.addAuthStateListener(listener)

            e.setCancellable { auth.removeAuthStateListener(listener) }
        }
    }

    override fun currentUser(): Observable<Optional<User>> {
        return currentFirebaseUser().map { it.map { it.toUser() } }
    }

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
