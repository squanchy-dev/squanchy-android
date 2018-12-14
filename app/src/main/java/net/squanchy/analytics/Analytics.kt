package net.squanchy.analytics

import android.app.Activity
import net.squanchy.signin.SignInOrigin
import net.squanchy.wificonfig.WifiConfigOrigin

interface Analytics {
    fun initializeStaticUserProperties()
    fun trackPageView(activity: Activity, screenName: String, screenClassOverride: String? = null)
    fun trackPageViewOnFirebaseAnalytics(activity: Activity, screenName: String, screenClassOverride: String?)
    fun trackPageViewOnCrashlytics(screenName: String)
    fun trackItemSelected(contentType: ContentType, itemId: String)
    fun trackItemSelectedOnFirebaseAnalytics(contentType: ContentType, itemId: String)
    fun trackItemSelectedOnCrashlytics(contentType: ContentType, itemId: String)
    fun enableExceptionLogging()
    fun trackFirstStartUserNotLoggedIn()
    fun trackFirstStartNotificationsEnabled()
    fun trackNotificationsEnabled()
    fun trackNotificationsDisabled()
    fun trackFavoritesInScheduleEnabled()
    fun trackFavoritesInScheduleDisabled()
    fun trackUserNotLoggedIn()
    fun trackUserLoggedInFrom(signInOrigin: SignInOrigin)
    fun setUserLoginProperty(loginStatus: LoginStatus)
    fun trackWifiConfigurationEvent(isSuccess: Boolean, wifiConfigOrigin: WifiConfigOrigin)
}
