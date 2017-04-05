package net.squanchy.settings;

import net.squanchy.injection.ActivityLifecycle;
import net.squanchy.injection.ApplicationComponent;
import net.squanchy.signin.SignInModule;
import net.squanchy.signin.SignInService;

import dagger.Component;

@ActivityLifecycle
@Component(modules = {SignInModule.class}, dependencies = ApplicationComponent.class)
interface SettingsActivityComponent {

    SignInService signInService();
}
