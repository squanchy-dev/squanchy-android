package net.squanchy.remoteconfig

import androidx.annotation.XmlRes

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

import net.squanchy.BuildConfig
import net.squanchy.R

internal class RemoteConfigBuilder(private val firebaseRemoteConfig: FirebaseRemoteConfig) {

    private var debugMode = BuildConfig.DEBUG

    @XmlRes
    private var defaultsResId = R.xml.remote_config_defaults

    fun withDebugMode(debugMode: Boolean): RemoteConfigBuilder {
        this.debugMode = debugMode
        return this
    }

    fun withDefaults(@XmlRes defaultsResId: Int): RemoteConfigBuilder {
        this.defaultsResId = defaultsResId
        return this
    }

    fun build(): RemoteConfig {
        val configSettings = buildRemoteConfigSettings(debugMode)
        firebaseRemoteConfig.setConfigSettings(configSettings)
        firebaseRemoteConfig.setDefaults(defaultsResId)

        return RemoteConfig(firebaseRemoteConfig, debugMode)
    }

    private fun buildRemoteConfigSettings(debugMode: Boolean): FirebaseRemoteConfigSettings {
        return FirebaseRemoteConfigSettings.Builder()
            .setDeveloperModeEnabled(debugMode)
            .build()
    }
}
