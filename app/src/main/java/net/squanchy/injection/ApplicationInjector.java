package net.squanchy.injection;

import android.content.Context;

import net.squanchy.SquanchyApplication;

public final class ApplicationInjector {

    private ApplicationInjector() {
        // no instances
    }

    public static ApplicationComponent obtain(Context context) {
        return ((SquanchyApplication) context.getApplicationContext()).applicationComponent();
    }
}
