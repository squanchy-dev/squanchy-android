package net.squanchy.remoteconfig

internal fun wifiAutoConfigEnabledNow(remoteConfig: RemoteConfig) = remoteConfig.getBoolean(KEY_WIFI_CONFIG_ENABLED)

internal fun obtainVenueWifiConfiguration(remoteConfig: RemoteConfig): WifiConfiguration {
    val ssid = remoteConfig.getString(KEY_WIFI_SSID)
    val password = remoteConfig.getString(KEY_WIFI_PASSWORD)
    return WifiConfiguration(ssid, password)
}

data class WifiConfiguration(val ssid: String, val password: String)

private const val KEY_WIFI_CONFIG_ENABLED = "wifi_config_enabled"
private const val KEY_WIFI_SSID = "wifi_ssid"
private const val KEY_WIFI_PASSWORD = "wifi_password"
