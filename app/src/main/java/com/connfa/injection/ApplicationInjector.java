package com.connfa.injection;

import android.content.Context;

import com.connfa.ConnfaApplication;

public final class ApplicationInjector {

    private ApplicationInjector() {
        // no instances
    }

    public static ApplicationComponent obtain(Context context) {
        return ((ConnfaApplication) context.getApplicationContext()).applicationComponent();
    }
}
