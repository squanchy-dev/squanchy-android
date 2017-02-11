package net.squanchy.speaker;

import android.content.Context;

import net.squanchy.navigation.Navigator;

class SpeakerNavigator implements Navigator {

    private final Context context;

    SpeakerNavigator(Context context) {
        this.context = context;
    }

    @Override
    public void up() {
        // No-op (top level yo)
    }

    @Override
    public void toEventDetails() {

    }
}
