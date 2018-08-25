package net.squanchy.venue

import androidx.appcompat.app.AppCompatActivity
import dagger.Component
import net.squanchy.injection.ActivityContextModule
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.injection.applicationComponent
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator

internal fun venueInfoComponent(activity: AppCompatActivity) =
    DaggerVenueInfoComponent.builder()
            .applicationComponent(activity.applicationComponent)
            .navigationModule(NavigationModule())
            .venueInfoModule(VenueInfoModule())
            .activityContextModule(ActivityContextModule(activity))
            .build()

@ActivityLifecycle
@Component(modules = [VenueInfoModule::class, NavigationModule::class], dependencies = [ApplicationComponent::class])
internal interface VenueInfoComponent {

    fun navigator(): Navigator

    fun service(): VenueInfoService
}
