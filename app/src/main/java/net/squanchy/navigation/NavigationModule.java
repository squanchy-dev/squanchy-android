package net.squanchy.navigation;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class NavigationModule {

    private final Context context;

    public NavigationModule(Context context) {
        this.context = context;
    }

    @Provides
    Context context() {
        return context;
    }

    @Provides
    Navigator navigator(Context context) {
        return new Navigator(context);
    }
}
