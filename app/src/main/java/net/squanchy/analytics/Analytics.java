package net.squanchy.analytics;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.CustomEvent;
import com.google.firebase.analytics.FirebaseAnalytics;

import timber.log.Timber;

public class Analytics {

    private final FirebaseAnalytics firebaseAnalytics;
    private final Crashlytics crashlytics;

    public static Analytics from(Context context) {
        Application application = (Application) context.getApplicationContext();
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        return new Analytics(firebaseAnalytics, Crashlytics.getInstance());
    }

    private Analytics(FirebaseAnalytics firebaseAnalytics, Crashlytics crashlytics) {
        this.firebaseAnalytics = firebaseAnalytics;
        this.crashlytics = crashlytics;
    }

    public void trackEvent(String category, String action) {
        trackEvent(category, action, null);
    }

    public void trackEvent(String category, String action, @Nullable String label) {
        trackOnFirebaseAnalytics(category, action, label);
        trackOnCrashlytics(action);
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

    public void enableExceptionLogging() {
        Timber.plant(new CrashlyticsErrorsTree());
    }
}
