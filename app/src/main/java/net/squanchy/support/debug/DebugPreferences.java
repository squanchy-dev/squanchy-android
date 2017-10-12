package net.squanchy.support.debug;

import android.content.Context;
import android.content.SharedPreferences;

import net.squanchy.BuildConfig;

public class DebugPreferences {

    private static final String PREFERENCES_NAME_DEBUG = "debug";
    private static final boolean IS_DEBUG = BuildConfig.DEBUG;

    private final SharedPreferences preferences;

    DebugPreferences(Context context) {
        this.preferences = context.getSharedPreferences(DebugPreferences.PREFERENCES_NAME_DEBUG, Context.MODE_PRIVATE);
    }
}
