package net.squanchy.signin

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import net.squanchy.service.firebase.FirebaseAuthService
import net.squanchy.support.lang.Optional

class SignInService(private val authService: FirebaseAuthService) {

    fun isSignedInToGoogle(): Maybe<Boolean> =
        currentUser()
            .first(Optional.absent())
            .filter { it.isPresent }
            .map { it.get() }
            .map { firebaseUser -> !firebaseUser.isAnonymous }

    fun signInAnonymouslyIfNecessary(): Completable {
        return authService.currentUser()
            .firstOrError()
            .flatMapCompletable { user ->
                if (user.isPresent) {
                    currentUser().firstOrError()
                        .flatMapCompletable { Completable.complete() }
                } else {
                    authService.signInAnonymously()
                }
            }
    }

    fun currentUser(): Observable<Optional<FirebaseUser>> {
        return authService.currentUser()
            .subscribeOn(Schedulers.io())
    }

    internal fun signInWithGoogle(account: GoogleSignInAccount): Completable {
        return authService.signInWithGoogle(account)
            .subscribeOn(Schedulers.io())
    }

    fun signOut(): Completable {
        return authService.signOut()
            .subscribeOn(Schedulers.io())
    }
}
