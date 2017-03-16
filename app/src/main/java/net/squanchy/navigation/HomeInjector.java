package net.squanchy.navigation;

import android.app.Activity;

import net.squanchy.injection.ApplicationInjector;

final class HomeInjector {

    private HomeInjector() {
        // no instances
    }

    public static HomeComponent obtain(Activity activity) {
        return DaggerHomeComponent.builder()
                .applicationComponent(ApplicationInjector.obtain(activity))
                .build();
    }
}
