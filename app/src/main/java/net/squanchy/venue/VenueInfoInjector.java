package net.squanchy.venue;

import android.app.Activity;

import net.squanchy.injection.ActivityContextModule;
import net.squanchy.injection.ApplicationInjector;
import net.squanchy.navigation.NavigationModule;

final class VenueInfoInjector {

    private VenueInfoInjector() {
        // no instances
    }

    public static VenueInfoComponent obtain(Activity activity) {
        return DaggerVenueInfoComponent.builder()
                .applicationComponent(ApplicationInjector.obtain(activity))
                .navigationModule(new NavigationModule())
                .venueInfoModule(new VenueInfoModule())
                .activityContextModule(new ActivityContextModule(activity))
                .build();
    }
}
