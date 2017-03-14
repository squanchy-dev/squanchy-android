package net.squanchy.proximity.near;

import net.squanchy.proximity.ProximityProvider;
import net.squanchy.proximity.ProximityService;

import dagger.Module;
import dagger.Provides;

@Module
public class NearITModule {

    @Provides
    ProximityService proximityService(ProximityProvider proximityProvider){
        return new NearITProximityService(proximityProvider);
    }

}
