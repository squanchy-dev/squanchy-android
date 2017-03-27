package net.squanchy.service.proximity.injection;

import android.app.Application;

import net.squanchy.R;
import net.squanchy.injection.ApplicationLifecycle;
import net.squanchy.proximity.ProximityProvider;
import net.squanchy.proximity.near.NearITProximityProvider;

import dagger.Module;
import dagger.Provides;
import it.near.sdk.NearItManager;

@Module
public class ProximityModule {

    private final Application application;

    public ProximityModule(Application application) {
        this.application = application;
    }

    @ApplicationLifecycle
    @Provides
    ProximityProvider proximityProvider() {
        return new NearITProximityProvider(
                new NearItManager(application, application.getString(R.string.nearit_api_key))
        );
    }

    @Provides
    ProximityService proximityService(ProximityProvider proximityProvider) {
        return new ProximityService(proximityProvider);
    }
}
