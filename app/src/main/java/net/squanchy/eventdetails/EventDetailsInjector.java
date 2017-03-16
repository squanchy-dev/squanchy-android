package net.squanchy.eventdetails;

import net.squanchy.injection.ActivityContextModule;
import net.squanchy.injection.ApplicationInjector;
import net.squanchy.navigation.NavigationModule;

final class EventDetailsInjector {

    private EventDetailsInjector() {
        // no instances
    }

    public static EventDetailsComponent obtain(EventDetailsActivity activity) {
        return DaggerEventDetailsComponent.builder()
                .applicationComponent(ApplicationInjector.obtain(activity))
                .eventDetailsModule(new EventDetailsModule())
                .activityContextModule(new ActivityContextModule(activity))
                .navigationModule(new NavigationModule())
                .build();
    }
}
