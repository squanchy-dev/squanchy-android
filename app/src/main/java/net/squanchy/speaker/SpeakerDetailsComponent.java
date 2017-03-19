package net.squanchy.speaker;

import net.squanchy.injection.ActivityLifecycle;
import net.squanchy.injection.ApplicationComponent;
import net.squanchy.navigation.NavigationModule;
import net.squanchy.navigation.Navigator;

import dagger.Component;

@ActivityLifecycle
@Component(modules = {SpeakerDetailsModule.class, NavigationModule.class}, dependencies = ApplicationComponent.class)
interface SpeakerDetailsComponent {

    SpeakerDetailsService service();

    Navigator navigator();
}
