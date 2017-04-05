package net.squanchy.tweets;

import android.support.v7.app.AppCompatActivity;

import net.squanchy.injection.ActivityContextModule;
import net.squanchy.injection.ApplicationInjector;
import net.squanchy.navigation.NavigationModule;

final class TwitterInjector {

    private TwitterInjector() {
        // no instances
    }

    public static TwitterComponent obtain(AppCompatActivity activity) {
        return DaggerTwitterComponent.builder()
                .applicationComponent(ApplicationInjector.obtain(activity))
                .activityContextModule(new ActivityContextModule(activity))
                .twitterModule(new TwitterModule())
                .navigationModule(new NavigationModule())
                .build();
    }
}
