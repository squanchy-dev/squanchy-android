package net.squanchy.tweets;

import android.app.Activity;

import net.squanchy.injection.ActivityContextModule;
import net.squanchy.injection.ApplicationInjector;
import net.squanchy.navigation.NavigationModule;

final class TwitterInjector {

    private TwitterInjector() {
        // no instances
    }

    public static TwitterComponent obtain(Activity activity) {
        return DaggerTwitterComponent.builder()
                .applicationComponent(ApplicationInjector.obtain(activity))
                .activityContextModule(new ActivityContextModule(activity))
                .twitterModule(new TwitterModule())
                .navigationModule(new NavigationModule())
                .build();
    }
}
