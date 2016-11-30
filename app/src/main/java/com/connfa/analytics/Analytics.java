package com.connfa.analytics;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;

import com.connfa.R;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.CustomEvent;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders.EventBuilder;
import com.google.android.gms.analytics.Tracker;

import timber.log.Timber;

public class Analytics {

    private final Tracker googleTracker;
    private final Crashlytics crashlytics;

    public static Analytics from(Context context) {
        Application application = (Application) context.getApplicationContext();
        Tracker tracker = GoogleAnalytics.getInstance(application)
                .newTracker(R.xml.global_tracker);
        return new Analytics(tracker, Crashlytics.getInstance());
    }

    private Analytics(Tracker googleTracker, Crashlytics crashlytics) {
        this.googleTracker = googleTracker;
        this.crashlytics = crashlytics;
    }

    public void trackEvent(String category, String action) {
        trackEvent(category, action, null);
    }

    public void trackEvent(String category, String action, @Nullable String label) {
        trackOnGoogleAnalytics(category, action, label);
        trackOnCrashlytics(action);
    }

    private void trackOnGoogleAnalytics(String category, String action, @Nullable String label) {
        EventBuilder eventBuilder = new EventBuilder(category, action);
        if (label != null) {
            eventBuilder.setLabel(label);
        }
        googleTracker.send(eventBuilder.build());
    }

    private void trackOnCrashlytics(String action) {
        CustomEvent event = new CustomEvent(action);
        crashlytics.answers.logCustom(event);
    }

    public void enableActivityLifecycleLogging() {
        googleTracker.enableAutoActivityTracking(true);
    }

    public void enableExceptionLogging() {
        Timber.plant(new CrashlyticsErrorsTree());
        googleTracker.enableExceptionReporting(true);
    }
}
