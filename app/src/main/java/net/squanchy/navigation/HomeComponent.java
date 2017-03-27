package net.squanchy.navigation;

import net.squanchy.analytics.Analytics;
import net.squanchy.injection.ActivityLifecycle;
import net.squanchy.injection.ApplicationComponent;
import net.squanchy.remoteconfig.RemoteConfig;
import net.squanchy.service.proximity.injection.ProximityService;

import dagger.Component;

@ActivityLifecycle
@Component(modules = {HomeModule.class, NavigationModule.class}, dependencies = ApplicationComponent.class)
interface HomeComponent {

    Analytics analytics();

    RemoteConfig remoteConfig();

    ProximityService proximityService();

    HomeService homeService();

    Navigator navigator();
}
