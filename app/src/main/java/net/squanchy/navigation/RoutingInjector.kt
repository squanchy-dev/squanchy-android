package net.squanchy.navigation

import android.support.v7.app.AppCompatActivity

import net.squanchy.injection.ActivityContextModule
import net.squanchy.injection.ApplicationInjector
import net.squanchy.navigation.deeplink.DeepLinkModule
import net.squanchy.signin.SignInModule

internal object RoutingInjector {

    fun obtain(activity: AppCompatActivity): RoutingComponent {
        return DaggerRoutingComponent.builder()
                .activityContextModule(ActivityContextModule(activity))
                .applicationComponent(ApplicationInjector.obtain(activity))
                .deepLinkModule(DeepLinkModule())
                .navigationModule(NavigationModule())
                .signInModule(SignInModule())
                .routingModule(RoutingModule())
                .build()
    }
}
