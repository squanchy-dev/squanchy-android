package net.squanchy.remoteconfig;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import net.squanchy.BuildConfig;
import net.squanchy.R;

import dagger.Module;
import dagger.Provides;

@Module
public class RemoteConfigModule {

    @Provides
    FirebaseRemoteConfig FirebaseRemoteConfig() {
        return FirebaseRemoteConfig.getInstance();
    }

    @Provides
    RemoteConfig remoteConfig(FirebaseRemoteConfig firebaseRemoteConfig) {
        return new RemoteConfigBuilder(firebaseRemoteConfig)
                .withDebugMode(BuildConfig.DEBUG)
                .withDefaults(R.xml.remote_config_defaults)
                .build();
    }
}
