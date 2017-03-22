package net.squanchy.support;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

public final class ContextUnwrapper {

    private ContextUnwrapper() {
        // Non-instantiable utility class
    }

    public static Activity unwrapToActivityContext(Context context) {
        if (context == null) {
            throw new NullPointerException("Context cannot be null");
        } else if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            ContextWrapper contextWrapper = (ContextWrapper) context;
            return unwrapToActivityContext(contextWrapper.getBaseContext());
        } else {
            throw new IllegalStateException("Context type not supported: " + context.getClass().getCanonicalName());
        }
    }
}
