package net.squanchy.analytics;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;
import com.google.firebase.analytics.FirebaseAnalytics;

import timber.log.Timber;

public class Analytics {

    private final FirebaseAnalytics firebaseAnalytics;
    private final Crashlytics crashlytics;

    public static Analytics from(Context context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        return new Analytics(firebaseAnalytics, Crashlytics.getInstance());
    }

    private Analytics(FirebaseAnalytics firebaseAnalytics, Crashlytics crashlytics) {
        this.firebaseAnalytics = firebaseAnalytics;
        this.crashlytics = crashlytics;
    }

    public void trackPageView(Activity activity, String screenName) {
        trackPageView(activity, screenName, null);
    }

    public void trackPageView(Activity activity, String screenName, @Nullable String screenClassOverride) {
        trackPageViewOnFirebaseAnalytics(activity, screenName, screenClassOverride);
        trackPageViewOnCrashlytics(screenName);
    }

    private void trackPageViewOnFirebaseAnalytics(Activity activity, String screenName, String screenClassOverride) {
        firebaseAnalytics.setCurrentScreen(activity, screenName, screenClassOverride);
    }

    private void trackPageViewOnCrashlytics(String screenName) {
        ContentViewEvent viewEvent = new ContentViewEvent();
        viewEvent.putContentName(screenName);
        crashlytics.answers.logContentView(viewEvent);
    }

    public void trackItemSelected(ContentType contentType, String itemId) {
        trackItemSelectedOnFirebaseAnalytics(contentType, itemId);
        trackItemSelectedOnCrashlytics(contentType, itemId);
    }

    private void trackItemSelectedOnFirebaseAnalytics(ContentType contentType, String itemId) {
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.CONTENT_TYPE, contentType.rawContentType());
        params.putString(FirebaseAnalytics.Param.ITEM_ID, itemId);
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, params);
    }

    private void trackItemSelectedOnCrashlytics(ContentType contentType, String itemId) {
        CustomEvent event = new CustomEvent("item_selected");
        event.putCustomAttribute("content_type", contentType.rawContentType());
        event.putCustomAttribute("item_id", itemId);
        crashlytics.answers.logCustom(event);
    }

    public void enableExceptionLogging() {
        Timber.plant(new CrashlyticsErrorsTree());
    }
}
