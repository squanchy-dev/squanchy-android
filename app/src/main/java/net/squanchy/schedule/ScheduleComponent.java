package net.squanchy.schedule;

import android.content.Context;

import net.squanchy.analytics.Analytics;
import net.squanchy.injection.ActivityLifecycle;
import net.squanchy.injection.ApplicationComponent;
import net.squanchy.navigation.NavigationModule;
import net.squanchy.navigation.Navigator;
import net.squanchy.service.proximity.injection.ProximityService;

import dagger.Component;

@ActivityLifecycle
@Component(modules = {ScheduleModule.class, NavigationModule.class}, dependencies = ApplicationComponent.class)
public interface ScheduleComponent {

    ScheduleService service();

    Context context();

    Navigator navigator();

    ProximityService proxService();

    Analytics analytics();
}
