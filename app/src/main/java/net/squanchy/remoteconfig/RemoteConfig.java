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

    private static final String KEY_PROXIMITY_ENABLED = "proximity_enabled";
    private static final String KEY_CONTEST_GOAL = "contest_goal";

    private final FirebaseRemoteConfig remoteConfig;
    private final boolean debugMode;

    RemoteConfig(FirebaseRemoteConfig remoteConfig, boolean debugMode) {
        this.remoteConfig = remoteConfig;
        this.debugMode = debugMode;
    }

    public Single<Boolean> proximityServicesEnabled() {
        return getConfigValue(() -> remoteConfig.getBoolean(KEY_PROXIMITY_ENABLED))
                .subscribeOn(Schedulers.io());
    }

    public Single<Long> contestGoal() {
        return getConfigValue(() -> remoteConfig.getLong(KEY_CONTEST_GOAL))
                .subscribeOn(Schedulers.io());
    }

    private <T> Single<T> getConfigValue(Func0<T> action) {
        return fetchAndActivate(cacheExpiryInSeconds())
                .andThen((SingleSource<T>) emitter -> action.call());
    }

    public Completable fetchNow() {
        return fetchAndActivate(EXPIRY_IMMEDIATELY)
                .subscribeOn(Schedulers.io());
    }

    private Completable fetchAndActivate(long cacheExpiryInSeconds) {
        return Completable.create(emitter -> remoteConfig.fetch(cacheExpiryInSeconds)
                .addOnCompleteListener(task -> {
                    remoteConfig.activateFetched();
                    emitter.onComplete();
                })
        );
    }

    private long cacheExpiryInSeconds() {
        return debugMode ? EXPIRY_IMMEDIATELY : EXPIRY_ONE_HOUR;
    }
}
