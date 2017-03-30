package net.squanchy.onboarding.location;

import net.squanchy.injection.ActivityLifecycle;
import net.squanchy.injection.ApplicationComponent;
import net.squanchy.onboarding.Onboarding;
import net.squanchy.onboarding.OnboardingModule;
import net.squanchy.service.proximity.injection.ProximityService;

import dagger.Component;

@ActivityLifecycle
@Component(modules = {OnboardingModule.class}, dependencies = ApplicationComponent.class)
public interface LocationOnboardingComponent {

    ProximityService proximityService();

    Onboarding onboarding();
}
