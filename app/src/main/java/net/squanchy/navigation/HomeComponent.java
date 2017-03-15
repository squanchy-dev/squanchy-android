package net.squanchy.navigation;

import net.squanchy.analytics.Analytics;
import net.squanchy.analytics.AnalyticsModule;
import net.squanchy.injection.ActivityLifecycle;

import dagger.Component;

@ActivityLifecycle
@Component(modules = {NavigationModule.class, AnalyticsModule.class})
public interface HomeComponent {

    Navigator navigator();

    Analytics analytics();
}
