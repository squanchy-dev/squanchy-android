package net.squanchy.remoteconfig;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.concurrent.TimeUnit;

import net.squanchy.support.lang.Func0;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.schedulers.Schedulers;

public class RemoteConfig {

    private static final long EXPIRY_IMMEDIATELY = TimeUnit.HOURS.toSeconds(0);
    private static final long EXPIRY_ONE_HOUR = TimeUnit.HOURS.toSeconds(1);

    private final FirebaseRemoteConfig firebaseRemoteConfig;
    private final boolean debugMode;

    RemoteConfig(FirebaseRemoteConfig firebaseRemoteConfig, boolean debugMode) {
        this.firebaseRemoteConfig = firebaseRemoteConfig;
        this.debugMode = debugMode;
    }

    // TODO put something here :D

    private <T> Single<T> getConfigValue(Func0<T> action) {
        return fetchAndActivate(cacheExpiryInSeconds())
                .andThen((SingleSource<T>) emitter -> emitter.onSuccess(action.call()));
    }

    public Completable fetchNow() {
        return fetchAndActivate(EXPIRY_IMMEDIATELY)
                .subscribeOn(Schedulers.io());
    }

    private Completable fetchAndActivate(long cacheExpiryInSeconds) {
        return Completable.create(emitter -> firebaseRemoteConfig.fetch(cacheExpiryInSeconds)
                .addOnCompleteListener(task -> {
                    firebaseRemoteConfig.activateFetched();
                    emitter.onComplete();
                })
                .addOnFailureListener(exception -> {
                    if (emitter.isDisposed()) {
                        return;
                    }
                    emitter.onError(exception);
                })
        );
    }

    private long cacheExpiryInSeconds() {
        return debugMode ? EXPIRY_IMMEDIATELY : EXPIRY_ONE_HOUR;
    }
}
