package net.squanchy.support.injection;

import net.squanchy.support.system.CurrentTime;

import dagger.Module;
import dagger.Provides;

@Module
public class CurrentTimeModule {

    @Provides
    CurrentTime provideCurrentTime() {
        return new CurrentTime();
    }
}
