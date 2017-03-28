package net.squanchy.tweets;

import android.app.Activity;

import net.squanchy.injection.ApplicationInjector;

final class TwitterInjector {

    private TwitterInjector() {
        // no instances
    }

    public static TwitterComponent obtain(Activity activity, String query) {
        return DaggerTwitterComponent.builder()
                .applicationComponent(ApplicationInjector.obtain(activity))
                .twitterModule(new TwitterModule(query))
                .build();
    }
}
