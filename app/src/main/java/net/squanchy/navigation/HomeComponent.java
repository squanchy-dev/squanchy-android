package net.squanchy.navigation;

import net.squanchy.analytics.Analytics;
import net.squanchy.injection.ActivityLifecycle;
import net.squanchy.injection.ApplicationComponent;

import dagger.Component;

@ActivityLifecycle
@Component(modules = {NavigationModule.class}, dependencies = {ApplicationComponent.class})
public interface HomeComponent {

    Navigator navigator();

    Analytics analytics();
}
