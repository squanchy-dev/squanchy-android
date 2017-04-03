package net.squanchy.settings;

import net.squanchy.injection.ActivityLifecycle;
import net.squanchy.injection.ApplicationComponent;
import net.squanchy.navigation.NavigationModule;
import net.squanchy.navigation.Navigator;
import net.squanchy.proximity.preconditions.OptInPreferencePersisterModule;
import net.squanchy.proximity.preconditions.ProximityOptInPersister;
import net.squanchy.proximity.preconditions.ProximityPreconditions;
import net.squanchy.proximity.preconditions.ProximityPreconditionsModule;
import net.squanchy.remoteconfig.RemoteConfig;
import net.squanchy.service.proximity.injection.ProximityService;
import net.squanchy.signin.SignInModule;
import net.squanchy.signin.SignInService;

import dagger.Component;

@ActivityLifecycle
@Component(modules = {SignInModule.class, NavigationModule.class, OptInPreferencePersisterModule.class, ProximityPreconditionsModule.class}, dependencies = ApplicationComponent.class)
public interface SettingsFragmentComponent {

    Navigator navigator();

    SignInService signInService();

    ProximityService proximityService();

    ProximityOptInPersister proximityOptInPersister();

    RemoteConfig remoteConfig();

    ProximityPreconditions proximityPreconditions();
}
