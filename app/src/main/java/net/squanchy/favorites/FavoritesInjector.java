package net.squanchy.favorites;

import android.app.Activity;

import net.squanchy.injection.ActivityContextModule;
import net.squanchy.injection.ApplicationInjector;
import net.squanchy.navigation.NavigationModule;

final class FavoritesInjector {

    private FavoritesInjector() {
        // no instances
    }

    public static FavoritesComponent obtain(Activity activity) {
        return DaggerFavoritesComponent.builder()
                .applicationComponent(ApplicationInjector.obtain(activity))
                .favoritesModule(new FavoritesModule())
                .navigationModule(new NavigationModule())
                .activityContextModule(new ActivityContextModule(activity))
                .build();
    }
}
