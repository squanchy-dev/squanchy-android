package net.squanchy.proximity.near;

import net.squanchy.injection.ActivityLifecycle;
import net.squanchy.injection.ApplicationComponent;
import net.squanchy.proximity.ProximityService;

import dagger.Component;

@ActivityLifecycle
@Component(modules = {NearITModule.class}, dependencies = ApplicationComponent.class)
public interface NearITComponent {

    ProximityService service();

}
