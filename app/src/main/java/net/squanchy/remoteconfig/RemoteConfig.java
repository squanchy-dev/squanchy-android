package net.squanchy.remoteconfig;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.concurrent.TimeUnit;

import net.squanchy.support.lang.Func0;

import io.reactivex.Single;

public class RemoteConfig {

    private static final long EXPIRY_IMMEDIATELY = TimeUnit.HOURS.toSeconds(0);
    private static final long EXPIRY_ONE_HOUR = TimeUnit.HOURS.toSeconds(1);

    private static final String KEY_GEOFENCE_ENABLED = "geofence_enabled";

    private final FirebaseRemoteConfig remoteConfig;
    private final boolean debugMode;

    RemoteConfig(FirebaseRemoteConfig remoteConfig, boolean debugMode) {
        this.remoteConfig = remoteConfig;
        this.debugMode = debugMode;
    }

    public Single<Boolean> proximityServicesEnabled() {
        return getBooleanConfigValue(() -> remoteConfig.getBoolean(KEY_GEOFENCE_ENABLED));
    }

    private <T> Single<T> getBooleanConfigValue(Func0<T> action) {
        return Single.create(emitter ->
                remoteConfig.fetch(cacheExpiryInSeconds())
                        .addOnCompleteListener(task -> emitter.onSuccess(action.call()))
        );
    }

    private long cacheExpiryInSeconds() {
        return debugMode ? EXPIRY_IMMEDIATELY : EXPIRY_ONE_HOUR;
    }
}
