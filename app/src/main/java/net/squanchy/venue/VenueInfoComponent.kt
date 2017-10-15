package net.squanchy.venue

import android.support.v7.app.AppCompatActivity
import dagger.Component
import net.squanchy.injection.ActivityContextModule
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.injection.ApplicationInjector
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator

internal fun venueInfoComponent(activity: AppCompatActivity): VenueInfoComponent {
    return DaggerVenueInfoComponent.builder()
            .applicationComponent(ApplicationInjector.obtain(activity))
            .navigationModule(NavigationModule())
            .venueInfoModule(VenueInfoModule())
            .activityContextModule(ActivityContextModule(activity))
            .build()
}

@ActivityLifecycle
@Component(modules = arrayOf(VenueInfoModule::class, NavigationModule::class), dependencies = arrayOf(ApplicationComponent::class))
internal interface VenueInfoComponent {

    fun navigator(): Navigator

    fun service(): VenueInfoService
}
