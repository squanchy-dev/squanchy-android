package net.squanchy.wificonfig

import android.app.Activity
import android.content.Context
import android.net.wifi.WifiManager
import dagger.Module
import dagger.Provides
import net.squanchy.injection.ActivityContextModule
import net.squanchy.remoteconfig.RemoteConfig

@Module(includes = [ActivityContextModule::class])
internal class WifiConfigModule {

    @Provides
    fun wifiManager(activity: Activity): WifiManager {
        return activity.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    @Provides
    fun service(wifiManager: WifiManager, remoteConfig: RemoteConfig) = WifiConfigService(wifiManager, remoteConfig)
}
