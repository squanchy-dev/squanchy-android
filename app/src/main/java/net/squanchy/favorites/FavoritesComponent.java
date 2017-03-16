package net.squanchy.favorites;

import android.content.Context;

import net.squanchy.analytics.Analytics;
import net.squanchy.injection.ActivityLifecycle;
import net.squanchy.injection.ApplicationComponent;
import net.squanchy.navigation.NavigationModule;
import net.squanchy.navigation.Navigator;
import net.squanchy.service.proximity.injection.ProximityService;

import dagger.Component;

@ActivityLifecycle
@Component(modules = {FavoritesModule.class, NavigationModule.class}, dependencies = ApplicationComponent.class)
interface FavoritesComponent {

    FavoritesService service();

    Context context();

    Navigator navigator();

    ProximityService proxService();

    Analytics analytics();
}
