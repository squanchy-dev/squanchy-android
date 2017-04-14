package net.squanchy.support;

import android.content.Context;
import android.content.ContextWrapper;
import android.support.v7.app.AppCompatActivity;

public final class ContextUnwrapper {

    private ContextUnwrapper() {
        // Non-instantiable utility class
    }

    public static AppCompatActivity unwrapToActivityContext(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        } else if (context instanceof AppCompatActivity) {
            return (AppCompatActivity) context;
        } else if (context instanceof ContextWrapper) {
            ContextWrapper contextWrapper = (ContextWrapper) context;
            return unwrapToActivityContext(contextWrapper.getBaseContext());
        } else {
            throw new IllegalStateException("Context type not supported: " + context.getClass().getCanonicalName());
        }
    }
}
