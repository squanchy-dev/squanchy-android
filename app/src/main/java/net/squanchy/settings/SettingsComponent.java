package net.squanchy.settings;

import net.squanchy.injection.ActivityLifecycle;
import net.squanchy.injection.ApplicationComponent;
import net.squanchy.navigation.NavigationModule;
import net.squanchy.navigation.Navigator;
import net.squanchy.proximity.preconditions.OptInPreferencePersister;
import net.squanchy.proximity.preconditions.OptInPreferencePersisterModule;
import net.squanchy.service.proximity.injection.ProximityService;
import net.squanchy.signin.SignInModule;
import net.squanchy.signin.SignInService;

import dagger.Component;

@ActivityLifecycle
@Component(modules = {SignInModule.class, NavigationModule.class, OptInPreferencePersisterModule.class}, dependencies = ApplicationComponent.class)
public interface SettingsComponent {

    Navigator navigator();

    SignInService signInService();

    ProximityService proximityService();

    OptInPreferencePersister optInPreferencePersister();
}
