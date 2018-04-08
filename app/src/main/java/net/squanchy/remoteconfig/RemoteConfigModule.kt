package net.squanchy.remoteconfig

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.Module
import dagger.Provides
import net.squanchy.BuildConfig
import net.squanchy.R

@Module
class RemoteConfigModule {

    @Provides
    fun firebaseRemoteConfig() = FirebaseRemoteConfig.getInstance()

    @Provides
    fun remoteConfig(firebaseRemoteConfig: FirebaseRemoteConfig): RemoteConfig {
        return RemoteConfigBuilder(firebaseRemoteConfig)
            .withDebugMode(BuildConfig.DEBUG)
            .withDefaults(R.xml.remote_config_defaults)
            .build()
    }

    @Provides
    fun featureFlags(remoteConfig: RemoteConfig) = FeatureFlags(remoteConfig)
}
