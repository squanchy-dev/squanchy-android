package net.squanchy.venue

import android.support.v7.app.AppCompatActivity

import net.squanchy.injection.ActivityContextModule
import net.squanchy.injection.ApplicationInjector
import net.squanchy.navigation.NavigationModule

internal object VenueInfoInjector {

    fun obtain(activity: AppCompatActivity): VenueInfoComponent {
        return DaggerVenueInfoComponent.builder()
                .applicationComponent(ApplicationInjector.obtain(activity))
                .navigationModule(NavigationModule())
                .venueInfoModule(VenueInfoModule())
                .activityContextModule(ActivityContextModule(activity))
                .build()
    }
}
