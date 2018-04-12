package net.squanchy.wificonfig

import android.net.wifi.WifiManager
import net.squanchy.analytics.Analytics
import net.squanchy.remoteconfig.RemoteConfig
import net.squanchy.remoteconfig.WifiConfiguration
import net.squanchy.remoteconfig.obtainVenueWifiConfiguration

class WifiConfigService(private val wifiManager: WifiManager, private val remoteConfig: RemoteConfig, private val analytics: Analytics) {

    fun setupWifi(wifiConfigOrigin: WifiConfigOrigin, callback: Callback) {
        val wifiConfiguration = obtainVenueWifiConfiguration(remoteConfig)
        val networkConfig = android.net.wifi.WifiConfiguration()
        networkConfig.SSID = "\"${wifiConfiguration.ssid}\""
        networkConfig.preSharedKey = "\"${wifiConfiguration.password}\""
        val netId = wifiManager.addNetwork(networkConfig)
        wifiManager.disconnect()
        val networkEnabled = wifiManager.enableNetwork(netId, true)
        wifiManager.reconnect()

        if (networkEnabled) {
            callback.onSuccess(wifiConfiguration)
        } else {
            callback.onFailure(wifiConfiguration)
        }
        analytics.trackWifiConfigurationEvent(networkEnabled, wifiConfigOrigin)
    }

    interface Callback {
        fun onSuccess(wifiConfiguration: WifiConfiguration)
        fun onFailure(wifiConfiguration: WifiConfiguration)
    }
}
