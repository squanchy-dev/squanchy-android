package net.squanchy.proximity.preconditions;

import net.squanchy.navigation.Navigator;

import dagger.Module;
import dagger.Provides;

@Module(includes = PreconditionsRegistryModule.class)
public class ProximityPreconditionsModule {

    private final ProximityPreconditions.Callback callback;

    public ProximityPreconditionsModule(ProximityPreconditions.Callback callback) {
        this.callback = callback;
    }

    @Provides
    ProximityPreconditions proximityPreconditions(PreconditionsRegistry registry, Navigator navigator) {
        return new ModularProximityPreconditions(registry, navigator, callback);
    }
}
