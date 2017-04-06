package net.squanchy.contest;

import net.squanchy.injection.ActivityLifecycle;
import net.squanchy.injection.ApplicationComponent;
import net.squanchy.support.debug.DebugPreferences;
import net.squanchy.support.debug.DebugPreferencesModule;
import net.squanchy.support.injection.CurrentTimeModule;

import dagger.Component;

@ActivityLifecycle
@Component(modules = {ContestModule.class, CurrentTimeModule.class, DebugPreferencesModule.class}, dependencies = ApplicationComponent.class)
public interface ContestComponent {

    ContestService contestService();

    DebugPreferences debugPreferences();
}
