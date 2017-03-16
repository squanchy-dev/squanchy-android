package net.squanchy.proximity.near;

import android.content.Context;

import net.squanchy.injection.ApplicationInjector;
import net.squanchy.navigation.DaggerHomeComponent;
import net.squanchy.navigation.HomeComponent;

public final class NearProximityServiceInjector {

    private NearProximityServiceInjector() {
    }

    public static HomeComponent obtain(Context context) {
        return DaggerHomeComponent.builder()
                .nearITModule(new NearITModule())
                .applicationComponent(ApplicationInjector.obtain(context))
                .build();
    }
}
