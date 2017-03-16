package net.squanchy.navigation;

import net.squanchy.injection.ActivityLifecycle;
import net.squanchy.injection.ApplicationComponent;
import net.squanchy.injection.ApplicationLifecycle;
import net.squanchy.service.proximity.injection.ProximityModule;
import net.squanchy.service.proximity.injection.ProximityService;

import dagger.Component;

@ActivityLifecycle
@Component(dependencies = ApplicationComponent.class)
public interface HomeComponent {

    ProximityService service();

}
