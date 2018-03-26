package net.squanchy.service.firebase

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import io.reactivex.Completable
import io.reactivex.CompletableSource
import io.reactivex.Observable
import net.squanchy.service.repository.AuthProvider
import net.squanchy.support.lang.Optional

class FirebaseAuthService(private val auth: AuthProvider) {

    fun signInWithGoogle(account: GoogleSignInAccount): Completable {
        return currentUser()
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
        return auth.signInWithCredential(credential)
    }

    private fun deleteUser(user: FirebaseUser): Completable {
        return user::delete.toCompletable()
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
        return currentUser()
            .filter { it.isPresent }
            .map { it.get() }
    }

    fun currentUser(): Observable<Optional<FirebaseUser>> {
        return auth.currentUser()
    }

    fun signOut(): Completable {
        return auth.signOut()
            .andThen(auth.signInAnonymously())
    }

    fun signInAnonymously(): Completable {
        return auth.signInAnonymously()
    }
}
