package net.squanchy.analytics;

import com.crashlytics.android.Crashlytics;

import net.squanchy.BuildConfig;

import timber.log.Timber;

public class CrashlyticsErrorsTree extends Timber.Tree {

    private static final boolean CAN_LOG = !BuildConfig.DEBUG;

    @Override
    protected boolean isLoggable(String tag, int priority) {
        return true;
    }

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        if (t != null && CAN_LOG) {
            Crashlytics.logException(t);
        }
    }
}
