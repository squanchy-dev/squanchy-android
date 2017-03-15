package net.squanchy.remoteconfig;

import android.support.annotation.XmlRes;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import net.squanchy.BuildConfig;
import net.squanchy.R;

class RemoteConfigBuilder {

    private final FirebaseRemoteConfig firebaseRemoteConfig;

    private boolean debugMode = BuildConfig.DEBUG;

    @XmlRes
    private int defaultsResId = R.xml.remote_config_defaults;

    RemoteConfigBuilder(FirebaseRemoteConfig firebaseRemoteConfig) {
        this.firebaseRemoteConfig = firebaseRemoteConfig;
    }

    RemoteConfigBuilder withDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
        return this;
    }

    RemoteConfigBuilder withDefaults(@XmlRes int defaultsResId) {
        this.defaultsResId = defaultsResId;
        return this;
    }

    public RemoteConfig build() {
        FirebaseRemoteConfigSettings configSettings = buildRemoteConfigSettings(debugMode);
        firebaseRemoteConfig.setConfigSettings(configSettings);
        firebaseRemoteConfig.setDefaults(defaultsResId);

        return new RemoteConfig(firebaseRemoteConfig, debugMode);
    }

    private FirebaseRemoteConfigSettings buildRemoteConfigSettings(boolean debugMode) {
        return new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(debugMode)
                .build();
    }
}
