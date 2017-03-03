package net.squanchy.schedule;

import android.content.Context;

import net.squanchy.injection.ActivityLifecycle;
import net.squanchy.injection.ApplicationComponent;
import net.squanchy.navigation.Navigator;

import dagger.Component;

@ActivityLifecycle
@Component(modules = {ScheduleModule.class}, dependencies = ApplicationComponent.class)
public interface ScheduleComponent {

    ScheduleService service();

    Context context();

    Navigator navigator();
}
