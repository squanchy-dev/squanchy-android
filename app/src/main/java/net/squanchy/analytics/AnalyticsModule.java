package net.squanchy.analytics;

import android.content.Context;

import net.squanchy.injection.ActivityContextModule;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ActivityContextModule.class})
public class AnalyticsModule {

    @Provides
    Analytics analytics(Context context) {
        return Analytics.from(context);
    }
}
