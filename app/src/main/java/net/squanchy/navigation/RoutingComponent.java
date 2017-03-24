package net.squanchy.navigation;

import net.squanchy.injection.ActivityLifecycle;
import net.squanchy.navigation.deeplink.DeepLinkModule;
import net.squanchy.navigation.deeplink.DeepLinkRouter;

import dagger.Component;

@ActivityLifecycle
@Component(modules = {DeepLinkModule.class})
public interface RoutingComponent {

    DeepLinkRouter deepLinkRouter();

    Navigator navigator();
}
