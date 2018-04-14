package net.squanchy.analytics

import android.app.Activity
import android.os.Bundle
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.answers.ContentViewEvent
import com.crashlytics.android.answers.CustomEvent
import com.google.firebase.analytics.FirebaseAnalytics
import net.squanchy.BuildConfig
import net.squanchy.signin.SignInOrigin
import net.squanchy.wificonfig.WifiConfigOrigin
import timber.log.Timber
import java.util.Locale

class Analytics internal constructor(
    private val firebaseAnalytics: FirebaseAnalytics,
    private val crashlytics: Crashlytics,
    private val firstStartDetector: FirstStartDetector
) {

    fun initializeStaticUserProperties() {
        firebaseAnalytics.setUserProperty("debug_build", BuildConfig.DEBUG.toString())
    }

    @JvmOverloads
    fun trackPageView(activity: Activity, screenName: String, screenClassOverride: String? = null) {
        trackPageViewOnFirebaseAnalytics(activity, screenName, screenClassOverride)
        trackPageViewOnCrashlytics(screenName)
    }

    private fun trackPageViewOnFirebaseAnalytics(activity: Activity, screenName: String, screenClassOverride: String?) {
        firebaseAnalytics.setCurrentScreen(activity, screenName, screenClassOverride)
    }

    private fun trackPageViewOnCrashlytics(screenName: String) {
        val viewEvent = ContentViewEvent()
        viewEvent.putContentName(screenName.toLowerCase(Locale.US))
        viewEvent.putContentId(screenName)
        viewEvent.putContentType("screen")
        crashlytics.answers.logContentView(viewEvent)
    }

    fun trackItemSelected(contentType: ContentType, itemId: String) {
        trackItemSelectedOnFirebaseAnalytics(contentType, itemId)
        trackItemSelectedOnCrashlytics(contentType, itemId)
    }

    private fun trackItemSelectedOnFirebaseAnalytics(contentType: ContentType, itemId: String) {
        val params = Bundle().apply {
            putString(FirebaseAnalytics.Param.CONTENT_TYPE, contentType.rawContentType)
            putString(FirebaseAnalytics.Param.ITEM_ID, itemId)
        }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, params)
    }

    private fun trackItemSelectedOnCrashlytics(contentType: ContentType, itemId: String) {
        val event = CustomEvent("item_selected")
        event.putCustomAttribute("content_type", contentType.rawContentType)
        event.putCustomAttribute("item_id", itemId)
        crashlytics.answers.logCustom(event)
    }

    fun enableExceptionLogging() {
        Timber.plant(CrashlyticsErrorsTree())
    }

    fun trackFirstStartUserNotLoggedIn() {
        if (firstStartDetector.isFirstStartNotLoggedInStatusTracked()) {
            return
        }
        trackUserNotLoggedIn()
        firstStartDetector.setFirstStartNotLoggedInStatusTracked()
    }

    fun trackFirstStartNotificationsEnabled() {
        if (firstStartDetector.isFirstStartNotificationsEnabledTracked()) {
            return
        }
        trackNotificationsEnabled()
        firstStartDetector.setFirstStartNotificationsEnabledTracked()
    }

    fun trackNotificationsEnabled() {
        firebaseAnalytics.setUserProperty("notifications_status", "enabled")
        firebaseAnalytics.logEvent("notifications_status_changed", flagEnabled("true"))
    }

    fun trackNotificationsDisabled() {
        firebaseAnalytics.setUserProperty("notifications_status", "disabled")
        firebaseAnalytics.logEvent("notifications_status_changed", flagEnabled("false"))
    }

    fun trackFavoritesInScheduleEnabled() {
        firebaseAnalytics.setUserProperty("favorites_in_schedule", "enable")
        firebaseAnalytics.logEvent("favorites_in_schedule_changed", flagEnabled("true"))
    }

    fun trackFavoritesInScheduleDisabled() {
        firebaseAnalytics.setUserProperty("favorites_in_schedule", "disabled")
        firebaseAnalytics.logEvent("favorites_in_schedule_changed", flagEnabled("false"))
    }

    private fun flagEnabled(value: String) = Bundle().apply { putString("enabled", value) }

    fun trackUserNotLoggedIn() {
        setUserLoginProperty(LoginStatus.NOT_LOGGED_IN)
    }

    fun trackUserLoggedInFrom(signInOrigin: SignInOrigin) {
        when (signInOrigin) {
            SignInOrigin.ONBOARDING -> setUserLoginProperty(LoginStatus.LOGGED_IN_ONBOARDING)
            SignInOrigin.FAVORITES -> setUserLoginProperty(LoginStatus.LOGGED_IN_FAVORITES)
            SignInOrigin.EVENT_DETAILS -> setUserLoginProperty(LoginStatus.LOGGED_IN_EVENT_DETAILS)
            SignInOrigin.SETTINGS -> setUserLoginProperty(LoginStatus.LOGGED_IN_SETTINGS)
        }
    }

    private fun setUserLoginProperty(loginStatus: LoginStatus) {
        firebaseAnalytics.setUserProperty("login_status", loginStatus.rawLoginStatus)
    }

    fun trackWifiConfigurationEvent(isSuccess: Boolean, wifiConfigOrigin: WifiConfigOrigin) {
        val params = Bundle().apply {
            putString("origin", wifiConfigOrigin.rawOrigin)
            putBoolean("success", isSuccess)
        }
        firebaseAnalytics.logEvent("wifi_config", params)
    }
}
