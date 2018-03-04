package net.squanchy.analytics

import android.app.Activity
import android.os.Bundle

import com.crashlytics.android.Crashlytics
import com.crashlytics.android.answers.ContentViewEvent
import com.crashlytics.android.answers.CustomEvent
import com.google.firebase.analytics.FirebaseAnalytics

import java.util.Locale

import net.squanchy.BuildConfig

import timber.log.Timber

class Analytics internal constructor(
        private val firebaseAnalytics: FirebaseAnalytics,
        private val crashlytics: Crashlytics
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
        val params = Bundle()
        params.putString(FirebaseAnalytics.Param.CONTENT_TYPE, contentType.rawContentType)
        params.putString(FirebaseAnalytics.Param.ITEM_ID, itemId)
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
}
