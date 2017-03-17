package net.squanchy.favorites;

import android.content.Context;

import net.squanchy.analytics.Analytics;
import net.squanchy.injection.ActivityLifecycle;
import net.squanchy.injection.ApplicationComponent;
import net.squanchy.navigation.NavigationModule;
import net.squanchy.navigation.Navigator;
import net.squanchy.schedule.ScheduleModule;
import net.squanchy.schedule.ScheduleService;

import dagger.Component;

@ActivityLifecycle
@Component(modules = {ScheduleModule.class, NavigationModule.class}, dependencies = ApplicationComponent.class)
interface FavoritesComponent {

    ScheduleService service();

    Context context();

    Navigator navigator();

    Analytics analytics();
}
