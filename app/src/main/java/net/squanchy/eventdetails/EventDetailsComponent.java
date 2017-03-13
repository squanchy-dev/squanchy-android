package net.squanchy.eventdetails;

import net.squanchy.injection.ActivityLifecycle;
import net.squanchy.injection.ApplicationComponent;
import net.squanchy.navigation.NavigationModule;
import net.squanchy.navigation.Navigator;

import dagger.Component;

@ActivityLifecycle
@Component(modules = {EventDetailsModule.class, NavigationModule.class}, dependencies = ApplicationComponent.class)
interface EventDetailsComponent {

    EventDetailsService service();

    Navigator navigator();
}
