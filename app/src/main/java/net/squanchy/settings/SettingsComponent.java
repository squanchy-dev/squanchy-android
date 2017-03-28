package net.squanchy.settings;

import net.squanchy.injection.ActivityLifecycle;
import net.squanchy.injection.ApplicationComponent;
import net.squanchy.navigation.NavigationModule;
import net.squanchy.navigation.Navigator;
import net.squanchy.signin.SignInModule;
import net.squanchy.signin.SignInService;

import dagger.Component;

@ActivityLifecycle
@Component(modules = {SignInModule.class, NavigationModule.class}, dependencies = ApplicationComponent.class)
public interface SettingsComponent {

    Navigator navigator();

    SignInService signInService();
}
