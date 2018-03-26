package net.squanchy.service.firebase

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Observable
import net.squanchy.service.repository.AuthProvider
import net.squanchy.support.lang.Optional

class FirebaseAuthProvider(private val auth: FirebaseAuth) : AuthProvider {

    override fun signInWithCredential(credential: AuthCredential): Completable {
        return { auth.signInWithCredential(credential) }.toCompletable()
    }

    override fun signOut(): Completable = Completable.fromAction(auth::signOut)

    override fun signInAnonymously(): Completable {
        return { auth.signInAnonymously() }.toCompletable()
    }

    override fun currentUser(): Observable<Optional<FirebaseUser>> {
        return Observable.create { e ->
            val listener = { firebaseAuth: FirebaseAuth -> e.onNext(Optional.fromNullable(firebaseAuth.currentUser)) }

            auth.addAuthStateListener(listener)

            e.setCancellable { auth.removeAuthStateListener(listener) }
        }
    }
}
