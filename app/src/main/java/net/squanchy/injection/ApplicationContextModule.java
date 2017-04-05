package net.squanchy.injection;

import android.app.Application;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationContextModule {

    private final Application application;

    ApplicationContextModule(Application application) {
        this.application = application;
    }

    @Provides
    Application applicationContext() {
        return application;
    }
}
