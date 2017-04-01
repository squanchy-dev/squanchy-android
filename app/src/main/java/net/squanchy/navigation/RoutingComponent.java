package net.squanchy.navigation;

import net.squanchy.injection.ActivityLifecycle;
import net.squanchy.injection.ApplicationComponent;
import net.squanchy.navigation.deeplink.DeepLinkModule;
import net.squanchy.navigation.deeplink.DeepLinkRouter;
import net.squanchy.signin.SignInModule;
import net.squanchy.signin.SignInService;

import dagger.Component;

@ActivityLifecycle
@Component(modules = {DeepLinkModule.class, SignInModule.class, RoutingModule.class}, dependencies = ApplicationComponent.class)
public interface RoutingComponent {

    DeepLinkRouter deepLinkRouter();

    Navigator navigator();

    SignInService signInService();

    FirstStartPersister firstStartPersister();
}
