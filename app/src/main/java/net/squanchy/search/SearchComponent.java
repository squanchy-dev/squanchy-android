package net.squanchy.search;

import net.squanchy.injection.ActivityLifecycle;
import net.squanchy.injection.ApplicationComponent;

import dagger.Component;

@ActivityLifecycle
@Component(modules = {SearchModule.class}, dependencies = ApplicationComponent.class)
interface SearchComponent {

    SearchService service();
}
