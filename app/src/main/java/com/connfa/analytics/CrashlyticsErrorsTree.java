package com.connfa.analytics;

import com.crashlytics.android.Crashlytics;

import timber.log.Timber;

public class CrashlyticsErrorsTree extends Timber.Tree {

    @Override
    protected boolean isLoggable(String tag, int priority) {
        return true;
    }

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        if (t != null) {
            Crashlytics.logException(t);
        }
    }
}
