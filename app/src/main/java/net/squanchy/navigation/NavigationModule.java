package net.squanchy.navigation;

import android.content.Context;

import net.squanchy.injection.ActivityContextModule;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ActivityContextModule.class})
public class NavigationModule {

    @Provides
    Navigator navigator(Context context) {
        return new Navigator(context);
    }
}
