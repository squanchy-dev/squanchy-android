package net.squanchy.venue

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import dagger.BindsInstance
import dagger.Component
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.injection.applicationComponent
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator

internal fun venueInfoComponent(activity: AppCompatActivity): VenueInfoComponent =
    DaggerVenueInfoComponent.builder()
        .applicationComponent(activity.applicationComponent)
        .activity(activity)
        .build()

@ActivityLifecycle
@Component(modules = [VenueInfoModule::class, NavigationModule::class], dependencies = [ApplicationComponent::class])
internal interface VenueInfoComponent {

    fun navigator(): Navigator

    fun service(): VenueInfoService

    @Component.Builder
    interface Builder {
        fun applicationComponent(applicationComponent: ApplicationComponent): Builder
        @BindsInstance
        fun activity(activity: Activity): Builder

        fun build(): VenueInfoComponent
    }
}
