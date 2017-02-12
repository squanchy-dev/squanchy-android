package net.squanchy.eventdetails;

import net.squanchy.injection.ActivityLifecycle;
import net.squanchy.injection.ApplicationComponent;

import dagger.Component;

@ActivityLifecycle
@Component(modules = {EventDetailsModule.class}, dependencies = ApplicationComponent.class)
interface EventDetailsComponent {

    EventDetailsService service();
}
