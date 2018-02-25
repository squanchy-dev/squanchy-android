package net.squanchy.schedule.filterschedule

import dagger.Component
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.injection.applicationComponent

@ActivityLifecycle
@Component(modules = [FilterScheduleModule::class], dependencies = [ApplicationComponent::class])
interface FilterScheduleComponent {

    fun service(): FilterScheduleService
}

internal fun filterScheduleComponent(dialog: FilterScheduleDialog): FilterScheduleComponent = DaggerFilterScheduleComponent.builder()
    .applicationComponent(dialog.context!!.applicationComponent)
    .filterScheduleModule(FilterScheduleModule())
    .build()