package net.squanchy.eventdetails;

import net.squanchy.injection.ApplicationInjector;

final class EventDetailsInjector {

    private EventDetailsInjector() {
        // no instances
    }

    public static EventDetailsComponent obtain(EventDetailsActivity activity) {
        return DaggerEventDetailsComponent.builder()
                .applicationComponent(ApplicationInjector.obtain(activity))
                .eventDetailsModule(new EventDetailsModule())
                .build();
    }
}
