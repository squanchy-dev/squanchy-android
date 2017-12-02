package net.squanchy.remoteconfig

import com.google.firebase.remoteconfig.FirebaseRemoteConfig

import net.squanchy.BuildConfig
import net.squanchy.R

import dagger.Module
import dagger.Provides

@Module
class RemoteConfigModule {

    @Provides
    internal fun firebaseRemoteConfig() = FirebaseRemoteConfig.getInstance()

    @Provides
    internal fun remoteConfig(firebaseRemoteConfig: FirebaseRemoteConfig): RemoteConfig {
        return RemoteConfigBuilder(firebaseRemoteConfig)
            .withDebugMode(BuildConfig.DEBUG)
            .withDefaults(R.xml.remote_config_defaults)
            .build()
    }
}
