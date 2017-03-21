package net.squanchy.venue;

import android.content.Context;

import net.squanchy.injection.ActivityLifecycle;
import net.squanchy.injection.ApplicationComponent;
import net.squanchy.navigation.NavigationModule;
import net.squanchy.navigation.Navigator;

import dagger.Component;

@ActivityLifecycle
@Component(modules = {NavigationModule.class}, dependencies = ApplicationComponent.class)
interface VenueInfoComponent {
    Context context();

    Navigator navigator();
}
