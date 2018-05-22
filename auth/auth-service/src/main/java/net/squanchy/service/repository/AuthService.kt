package net.squanchy.service.repository

import arrow.core.Option
import io.reactivex.Completable
import io.reactivex.Observable

interface AuthService {

    fun signInWithGoogle(accountIdToken: String): Completable

    fun <T> ifUserSignedInThenObservableFrom(observable: (String) -> Observable<T>): Observable<T>

    fun ifUserSignedInThenCompletableFrom(completable: (String) -> Completable): Completable

    fun currentUser(): Observable<Option<User>>

    fun signOut(): Completable

    fun signInAnonymously(): Completable
}
