package net.squanchy.analytics

import android.app.Activity
import net.squanchy.signin.SignInOrigin
import net.squanchy.wificonfig.WifiConfigOrigin
import timber.log.Timber

class DisabledAnalytics : Analytics {
    override fun initializeStaticUserProperties() = Unit

    override fun trackPageView(activity: Activity, screenName: String, screenClassOverride: String?) = Unit

    override fun trackPageViewOnFirebaseAnalytics(activity: Activity, screenName: String, screenClassOverride: String?) = Unit

    override fun trackPageViewOnCrashlytics(screenName: String) = Unit

    override fun trackItemSelected(contentType: ContentType, itemId: String) = Unit

    override fun trackItemSelectedOnFirebaseAnalytics(contentType: ContentType, itemId: String) = Unit

    override fun trackItemSelectedOnCrashlytics(contentType: ContentType, itemId: String) = Unit

    override fun setupExceptionLogging() {
        Timber.forest()
            .find { it is CrashlyticsErrorsTree }
            ?.let { Timber.uproot(it) }
    }

    override fun trackFirstStartUserNotLoggedIn() = Unit

    override fun trackFirstStartNotificationsEnabled() = Unit

    override fun trackNotificationsEnabled() = Unit

    override fun trackNotificationsDisabled() = Unit

    override fun trackFavoritesInScheduleEnabled() = Unit

    override fun trackFavoritesInScheduleDisabled() = Unit

    override fun trackUserNotLoggedIn() = Unit

    override fun trackUserLoggedInFrom(signInOrigin: SignInOrigin) = Unit

    override fun setUserLoginProperty(loginStatus: LoginStatus) = Unit

    override fun trackWifiConfigurationEvent(isSuccess: Boolean, wifiConfigOrigin: WifiConfigOrigin) = Unit
}
