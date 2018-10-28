package net.squanchy.schedule

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import dagger.BindsInstance
import dagger.Component
import net.squanchy.analytics.Analytics
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.injection.applicationComponent
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator
import net.squanchy.remoteconfig.FeatureFlags
import net.squanchy.schedule.tracksfilter.TracksFilter
import net.squanchy.service.repository.TracksRepository
import net.squanchy.support.injection.CurrentTimeModule
import net.squanchy.support.system.CurrentTime

internal fun scheduleComponent(activity: AppCompatActivity): ScheduleComponent = DaggerScheduleComponent.builder()
    .applicationComponent(activity.applicationComponent)
    .activity(activity)
    .build()

@ActivityLifecycle
@Component(
    modules = [ScheduleModule::class, NavigationModule::class, CurrentTimeModule::class],
    dependencies = [ApplicationComponent::class]
)
internal interface ScheduleComponent {

    fun scheduleService(): ScheduleService

    fun navigator(): Navigator

    fun analytics(): Analytics

    fun currentTime(): CurrentTime

    fun tracksRepository(): TracksRepository

    fun tracksFilter(): TracksFilter

    fun featureFlags(): FeatureFlags

    @Component.Builder
    interface Builder {
        fun applicationComponent(applicationComponent: ApplicationComponent): Builder
        @BindsInstance
        fun activity(activity: Activity): Builder

        fun build(): ScheduleComponent
    }
}
