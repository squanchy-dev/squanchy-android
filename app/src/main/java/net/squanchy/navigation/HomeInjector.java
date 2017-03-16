package net.squanchy.navigation;

import android.content.Context;

import net.squanchy.injection.ApplicationInjector;
import net.squanchy.service.proximity.injection.ProximityModule;

public final class HomeInjector {

    private HomeInjector() {
    }

    public static HomeComponent obtain(Context context) {
        return DaggerHomeComponent.builder()
                .applicationComponent(ApplicationInjector.obtain(context))
                .build();
    }
}
