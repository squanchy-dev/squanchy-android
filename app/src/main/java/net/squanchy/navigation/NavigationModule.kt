package net.squanchy.navigation

import android.app.Activity
import dagger.Module
import dagger.Provides
import net.squanchy.injection.ActivityContextModule

@Module(includes = arrayOf(ActivityContextModule::class))
internal class NavigationModule {

    @Provides
    internal fun debugActivityIntentFactory(): DebugActivityIntentFactory {
        return DebugActivityIntentFactory()
    }

    @Provides
    fun navigator(activity: Activity, debugActivityIntentFactory: DebugActivityIntentFactory): Navigator {
        return Navigator(activity, debugActivityIntentFactory)
    }
}
