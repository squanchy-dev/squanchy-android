package net.squanchy.schedule;

import net.squanchy.injection.ActivityLifecycle;
import net.squanchy.injection.ApplicationComponent;

import dagger.Component;

@ActivityLifecycle
@Component(modules = {ScheduleModule.class}, dependencies = ApplicationComponent.class)
interface ScheduleComponent {

    ScheduleService service();

    ScheduleNavigator navigator();
}
