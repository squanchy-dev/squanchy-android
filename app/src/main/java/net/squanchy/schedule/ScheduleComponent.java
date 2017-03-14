package net.squanchy.schedule;

import android.content.Context;

import net.squanchy.injection.ActivityLifecycle;
import net.squanchy.injection.ApplicationComponent;
import net.squanchy.navigation.Navigator;
import net.squanchy.proximity.ProximityService;
import net.squanchy.proximity.near.NearITModule;

import dagger.Component;

@ActivityLifecycle
@Component(modules = {ScheduleModule.class, NearITModule.class}, dependencies = ApplicationComponent.class)
public interface ScheduleComponent {

    ScheduleService service();

    Context context();

    Navigator navigator();

    ProximityService proxService();
}
