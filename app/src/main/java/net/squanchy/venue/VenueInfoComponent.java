package net.squanchy.venue;

import net.squanchy.injection.ActivityLifecycle;
import net.squanchy.injection.ApplicationComponent;
import net.squanchy.navigation.NavigationModule;
import net.squanchy.navigation.Navigator;

import dagger.Component;

@ActivityLifecycle
@Component(modules = {VenueInfoModule.class, NavigationModule.class}, dependencies = ApplicationComponent.class)
interface VenueInfoComponent {

    Navigator navigator();

    VenueInfoService service();
}
