package net.squanchy.proximity.near;

import android.content.Context;

import net.squanchy.injection.ApplicationInjector;


public final class NearProximityServiceInjector {

    private NearProximityServiceInjector() {
    }

    public static NearITComponent obtain(Context context) {
        return DaggerNearITComponent.builder()
                .nearITModule(new NearITModule())
                .applicationComponent(ApplicationInjector.obtain(context))
                .build();
    }
}
