package net.squanchy.analytics

import android.app.Application
import android.content.Context
import android.preference.PreferenceManager
import com.crashlytics.android.Crashlytics
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides
import net.squanchy.R

@Module
class AnalyticsModule {

    @Provides
    internal fun firebaseAnalytics(application: Application): FirebaseAnalytics = FirebaseAnalytics.getInstance(application)

    @Provides
    internal fun crashlytics(): Crashlytics = Crashlytics.getInstance()

    @Provides
    internal fun firstStartDetector(application: Application): FirstStartDetector {
        val preferences = application.getSharedPreferences(FIRST_START_DETECTOR_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        return FirstStartDetector(preferences)
    }

    @Provides
    internal fun analytics(
        application: Application,
        firebaseAnalytics: FirebaseAnalytics,
        crashlytics: Crashlytics,
        firstStartDetector: FirstStartDetector
    ): Analytics {
        if (analyticsDisabledByUser(application)) {
            return DisabledAnalytics()
        }

        return EnabledAnalytics(firebaseAnalytics, crashlytics, firstStartDetector)
    }

    private fun analyticsDisabledByUser(application: Application): Boolean {
        val preferenceKey = application.getString(R.string.disable_analytics_key)
        return PreferenceManager.getDefaultSharedPreferences(application)
            .getBoolean(preferenceKey, false)
    }

    companion object {

        private const val FIRST_START_DETECTOR_SHARED_PREFERENCES_NAME = "first_start_detector"
    }
}
