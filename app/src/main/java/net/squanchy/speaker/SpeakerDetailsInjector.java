package net.squanchy.speaker;

import net.squanchy.injection.ActivityContextModule;
import net.squanchy.injection.ApplicationInjector;
import net.squanchy.navigation.NavigationModule;

final class SpeakerDetailsInjector {

    private SpeakerDetailsInjector() {
        // no instances
    }

    public static SpeakerDetailsComponent obtain(SpeakerDetailsActivity activity) {
        return DaggerSpeakerDetailsComponent.builder()
                .applicationComponent(ApplicationInjector.obtain(activity))
                .speakerDetailsModule(new SpeakerDetailsModule())
                .activityContextModule(new ActivityContextModule(activity))
                .navigationModule(new NavigationModule())
                .build();
    }
}
