package net.squanchy.injection;

import android.content.Context;

import net.squanchy.ConnfaApplication;

public final class ApplicationInjector {

    private ApplicationInjector() {
        // no instances
    }

    public static ApplicationComponent obtain(Context context) {
        return ((ConnfaApplication) context.getApplicationContext()).applicationComponent();
    }
}
