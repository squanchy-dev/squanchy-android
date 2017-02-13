package net.squanchy.speaker;

import net.squanchy.injection.ActivityLifecycle;
import net.squanchy.injection.ApplicationComponent;

import dagger.Component;

@ActivityLifecycle
@Component(modules = {SpeakerModule.class}, dependencies = ApplicationComponent.class)
interface SpeakerComponent {

    SpeakerService service();
}
