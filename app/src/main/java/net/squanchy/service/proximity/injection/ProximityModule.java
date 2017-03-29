package net.squanchy.service.proximity.injection;

import android.content.Context;

import net.squanchy.R;
import net.squanchy.injection.ApplicationLifecycle;
import net.squanchy.injection.ContextModule;
import net.squanchy.proximity.ProximityProvider;
import net.squanchy.proximity.near.NearITProximityProvider;

import dagger.Module;
import dagger.Provides;
import it.near.sdk.NearItManager;

@Module(includes = {ContextModule.class})
public class ProximityModule {

    @ApplicationLifecycle
    @Provides
    ProximityProvider proximityProvider(Context context) {
        return new NearITProximityProvider(
                new NearItManager(context, context.getString(R.string.nearit_api_key))
        );
    }

    @Provides
    ProximityService proximityService(ProximityProvider proximityProvider) {
        return new ProximityService(proximityProvider);
    }
}
