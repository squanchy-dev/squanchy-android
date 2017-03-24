package net.squanchy.navigation;

import android.content.Context;
import android.content.Intent;

import net.squanchy.DebugActivity;

class DebugActivityIntentFactory {

    Intent createDebugActivityIntent(Context context) {
        throw new UnsupportedOperationException("The Debug activity is not available on release builds");
    }
}
