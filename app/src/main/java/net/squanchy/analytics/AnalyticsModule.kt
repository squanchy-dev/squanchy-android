package net.squanchy.analytics

import android.app.Application
import android.content.Context
import com.crashlytics.android.Crashlytics
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides

@Module
class AnalyticsModule(private val application: Application) {

    @Provides
    internal fun firebaseAnalytics(): FirebaseAnalytics = FirebaseAnalytics.getInstance(application)

    @Provides
    internal fun crashlytics(): Crashlytics = Crashlytics.getInstance()

    @Provides
    internal fun firstStartDetector(application: Application): FirstStartDetector {
        val preferences = application.getSharedPreferences(FIRST_START_DETECTOR_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        return FirstStartDetector(preferences)
    }

    @Provides
    internal fun analytics(
        firebaseAnalytics: FirebaseAnalytics,
        crashlytics: Crashlytics,
        firstStartDetector: FirstStartDetector
    ): Analytics {
        return Analytics(firebaseAnalytics, crashlytics, firstStartDetector)
    }

    companion object {

        private const val FIRST_START_DETECTOR_SHARED_PREFERENCES_NAME = "first_start_detector"
    }
}
