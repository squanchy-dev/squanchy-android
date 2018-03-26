package net.squanchy.service.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Observable
import net.squanchy.support.lang.Optional

interface AuthProvider {

    fun signInAnonymously(): Completable

    fun signInWithCredential(credential: AuthCredential): Completable

    fun signOut(): Completable

    fun currentUser(): Observable<Optional<FirebaseUser>>
}
