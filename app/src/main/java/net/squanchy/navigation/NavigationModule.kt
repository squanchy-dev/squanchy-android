package net.squanchy.navigation

import android.app.Activity
import dagger.Module
import dagger.Provides

@Module
internal class NavigationModule {

    @Provides
    internal fun debugActivityIntentFactory() = DebugActivityIntentFactory()

    @Provides
    fun navigator(activity: Activity, debugActivityIntentFactory: DebugActivityIntentFactory) = Navigator(activity, debugActivityIntentFactory)
}
