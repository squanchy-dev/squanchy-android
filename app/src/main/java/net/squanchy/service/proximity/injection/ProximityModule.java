package net.squanchy.service.proximity.injection;

import android.content.Context;

import net.squanchy.injection.ApplicationLifecycle;
import net.squanchy.proximity.ProximityProvider;
import net.squanchy.proximity.near.NearITProximityProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class ProximityModule {

    private final Context context;

    public ProximityModule(Context context) {
        this.context = context;
    }

    @ApplicationLifecycle
    @Provides
    ProximityProvider proximityProvider(){
        return new NearITProximityProvider(context);
    }
}
