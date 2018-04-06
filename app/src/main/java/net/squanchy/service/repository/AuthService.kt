package net.squanchy.service.repository

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import io.reactivex.Completable
import io.reactivex.Observable
import net.squanchy.support.lang.Optional

interface AuthService {

    fun signInWithGoogle(account: GoogleSignInAccount): Completable

    fun <T> ifUserSignedInThenObservableFrom(observable: (String) -> Observable<T>): Observable<T>

    fun ifUserSignedInThenCompletableFrom(completable: (String) -> Completable): Completable

    fun currentUser(): Observable<Optional<User>>

    fun signOut(): Completable

    fun signInAnonymously(): Completable
}
