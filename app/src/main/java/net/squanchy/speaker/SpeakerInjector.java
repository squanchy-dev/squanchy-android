package net.squanchy.speaker;

import net.squanchy.injection.ApplicationInjector;

final class SpeakerInjector {

    private SpeakerInjector() {
        // no instances
    }

    public static SpeakerComponent obtain(SpeakerListActivity activity) {
        return DaggerSpeakerComponent.builder()
                .applicationComponent(ApplicationInjector.obtain(activity))
                .speakerModule(new SpeakerModule(activity))
                .build();
    }
}
