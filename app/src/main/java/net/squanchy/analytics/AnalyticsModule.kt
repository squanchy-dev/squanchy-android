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
    internal fun firstStartUserPropertiesPersister(application: Application): FirstStartUserPropertiesPersister {
        val preferences = application.getSharedPreferences(FIRST_START_USER_PROPERTIES_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        return FirstStartUserPropertiesPersister(preferences)
    }

    @Provides
    internal fun analytics(
        firebaseAnalytics: FirebaseAnalytics,
        crashlytics: Crashlytics,
        firstStartUserPropertiesPersister: FirstStartUserPropertiesPersister
    ): Analytics {
        return Analytics(firebaseAnalytics, crashlytics, firstStartUserPropertiesPersister)
    }

    companion object {

        private const val FIRST_START_USER_PROPERTIES_SHARED_PREFERENCES_NAME = "first_start_user_properties"
    }
}
