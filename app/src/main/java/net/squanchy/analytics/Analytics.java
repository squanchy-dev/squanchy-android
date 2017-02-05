package net.squanchy.analytics;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.CustomEvent;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders.EventBuilder;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;

import net.squanchy.R;

import timber.log.Timber;

public class Analytics {

    private final Tracker googleTracker;
    private final FirebaseAnalytics firebaseAnalytics;
    private final Crashlytics crashlytics;

    public static Analytics from(Context context) {
        Application application = (Application) context.getApplicationContext();
        Tracker tracker = GoogleAnalytics.getInstance(application)
                .newTracker(R.xml.global_tracker);
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        return new Analytics(tracker, firebaseAnalytics, Crashlytics.getInstance());
    }

    private Analytics(Tracker googleTracker, FirebaseAnalytics firebaseAnalytics, Crashlytics crashlytics) {
        this.googleTracker = googleTracker;
        this.firebaseAnalytics = firebaseAnalytics;
        this.crashlytics = crashlytics;
    }

    public void trackEvent(String category, String action) {
        trackEvent(category, action, null);
    }

    public void trackEvent(String category, String action, @Nullable String label) {
        trackOnGoogleAnalytics(category, action, label);
        trackOnFirebaseAnalytics(category, action, label);
        trackOnCrashlytics(action);
    }

    private void trackOnGoogleAnalytics(String category, String action, @Nullable String label) {
        EventBuilder eventBuilder = new EventBuilder(category, action);
        if (label != null) {
            eventBuilder.setLabel(label);
        }
        googleTracker.send(eventBuilder.build());
    }

    private void trackOnFirebaseAnalytics(String category, String action, @Nullable String label) {
        Bundle bundle = new Bundle();
        bundle.putString("action", action);
        if (label != null) {
            bundle.putString("label", label);
        }
        firebaseAnalytics.logEvent(category, bundle);
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
