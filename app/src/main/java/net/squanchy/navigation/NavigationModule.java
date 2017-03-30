package net.squanchy.navigation;

import android.app.Activity;

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
    public Navigator navigator(Activity activity, DebugActivityIntentFactory debugActivityIntentFactory) {
        return new Navigator(activity, debugActivityIntentFactory);
    }
}
