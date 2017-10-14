package net.squanchy.onboarding.account;

import net.squanchy.injection.ActivityLifecycle;
import net.squanchy.injection.ApplicationComponent;
import net.squanchy.navigation.NavigationModule;
import net.squanchy.navigation.Navigator;
import net.squanchy.onboarding.Onboarding;
import net.squanchy.onboarding.OnboardingModule;
import net.squanchy.signin.SignInModule;
import net.squanchy.signin.SignInService;

import dagger.Component;

@ActivityLifecycle
@Component(
        modules = {OnboardingModule.class, SignInModule.class, NavigationModule.class},
        dependencies = ApplicationComponent.class
)
public interface AccountOnboardingComponent {

    Onboarding onboarding();

    SignInService signInService();

    Navigator navigator();
}
