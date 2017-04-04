package net.squanchy.navigation;

import android.content.Context;
import android.content.Intent;

import net.squanchy.support.debug.DebugActivity;

class DebugActivityIntentFactory {

    Intent createDebugActivityIntent(Context context) {
        return new Intent(context, DebugActivity.class);
    }
}
