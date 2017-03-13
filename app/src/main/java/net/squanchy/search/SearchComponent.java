package net.squanchy.search;

import net.squanchy.injection.ActivityLifecycle;
import net.squanchy.injection.ApplicationComponent;
import net.squanchy.navigation.NavigationModule;
import net.squanchy.navigation.Navigator;

import dagger.Component;

@ActivityLifecycle
@Component(modules = {SearchModule.class, NavigationModule.class}, dependencies = ApplicationComponent.class)
interface SearchComponent {

    SearchService service();

    Navigator navigator();
}
