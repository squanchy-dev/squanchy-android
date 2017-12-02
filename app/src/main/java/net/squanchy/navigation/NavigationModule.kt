package net.squanchy.navigation

import android.app.Activity
import dagger.Module
import dagger.Provides
import net.squanchy.injection.ActivityContextModule

@Module(includes = [ActivityContextModule::class])
internal class NavigationModule {

    @Provides
    internal fun debugActivityIntentFactory() = DebugActivityIntentFactory()

    @Provides
    fun navigator(activity: Activity, debugActivityIntentFactory: DebugActivityIntentFactory) = Navigator(activity, debugActivityIntentFactory)
}
