package net.squanchy.navigation.deeplink

import android.app.Activity
import dagger.Module
import dagger.Provides
import net.squanchy.R
import net.squanchy.injection.ActivityContextModule
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator

@Module(includes = [ActivityContextModule::class, NavigationModule::class])
class DeepLinkModule {

    @Provides
    internal fun deepLinkScheme(activity: Activity) = activity.getString(R.string.deeplink_scheme)

    @Provides
    internal fun deepLinkNavigator(navigator: Navigator) = DeepLinkNavigator(navigator)

    @Provides
    internal fun deepLinkRouter(deepLinkScheme: String, navigator: DeepLinkNavigator) = DeepLinkRouter(deepLinkScheme, navigator)
}
