package net.squanchy

import arrow.core.Option
import io.reactivex.Completable
import io.reactivex.Observable
import net.squanchy.service.repository.AuthService
import net.squanchy.service.repository.User

class FakeAuthService(
    private val uid: String = "uid"
) : AuthService {

    override fun signInWithGoogle(accountIdToken: String): Completable {
        TODO("not implemented")
    }

    override fun <T> ifUserSignedInThenObservableFrom(observable: (String) -> Observable<T>): Observable<T> {
        return observable(uid)
    }

    override fun ifUserSignedInThenCompletableFrom(completable: (String) -> Completable): Completable {
        return completable(uid)
    }

    override fun currentUser(): Observable<Option<User>> {
        TODO("not implemented")
    }

    override fun signOut(): Completable {
        TODO("not implemented")
    }

    override fun signInAnonymously(): Completable {
        TODO("not implemented")
    }
}
