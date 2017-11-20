package net.squanchy.remoteconfig

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import net.squanchy.support.lang.Func0
import java.util.concurrent.TimeUnit

class RemoteConfig(
        private val firebaseRemoteConfig: FirebaseRemoteConfig,
        private val debugMode: Boolean
) {

    private val cacheExpiryInSeconds: Long
        get() = if (debugMode) EXPIRY_IMMEDIATELY else EXPIRY_ONE_HOUR

    private fun <T> getConfigValue(action: Func0<T>): Single<T> {
        return fetchAndActivate(cacheExpiryInSeconds)
            .andThen(Single.just(action.call()))
    }

    fun fetchNow(): Completable {
        return fetchAndActivate(EXPIRY_IMMEDIATELY)
            .subscribeOn(Schedulers.io())
    }

    private fun fetchAndActivate(cacheExpiryInSeconds: Long): Completable {
        return Completable.create { emitter ->
            firebaseRemoteConfig.fetch(cacheExpiryInSeconds)
                .addOnCompleteListener {
                    firebaseRemoteConfig.activateFetched()
                    emitter.onComplete()
                }
                .addOnFailureListener { exception ->
                    if (emitter.isDisposed) {
                        return@addOnFailureListener
                    }
                    emitter.onError(exception)
                }
        }
    }

    companion object {

        private val EXPIRY_IMMEDIATELY = TimeUnit.HOURS.toSeconds(0)
        private val EXPIRY_ONE_HOUR = TimeUnit.HOURS.toSeconds(1)
    }
}
