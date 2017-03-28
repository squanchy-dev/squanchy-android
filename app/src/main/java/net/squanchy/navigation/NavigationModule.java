package net.squanchy.navigation;

import android.content.Context;

import net.squanchy.injection.ActivityContextModule;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ActivityContextModule.class})
public class NavigationModule {

    @Provides
    DebugActivityIntentFactory debugActivityIntentFactory() {
        return new DebugActivityIntentFactory();
    }

    @Provides
    public Navigator navigator(Context context, DebugActivityIntentFactory debugActivityIntentFactory) {
        return new Navigator(context, debugActivityIntentFactory);
    }
}
