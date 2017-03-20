package net.squanchy.injection;

import android.app.Service;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class ServiceContextModule {

    private final Service service;

    public ServiceContextModule(Service service) {
        this.service = service;
    }

    @Provides
    public Context serviceContext() {
        return service;
    }
}
