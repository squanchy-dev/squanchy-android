package net.squanchy.analytics

import android.app.Application

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
    internal fun analytics(firebaseAnalytics: FirebaseAnalytics, crashlytics: Crashlytics): Analytics {
        return Analytics(firebaseAnalytics, crashlytics)
    }
}
