package net.squanchy.analytics;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;
import com.google.firebase.analytics.FirebaseAnalytics;

import net.squanchy.BuildConfig;
import net.squanchy.proximity.ProximityEvent;

import timber.log.Timber;

public class Analytics {

    private final FirebaseAnalytics firebaseAnalytics;
    private final Crashlytics crashlytics;
    private final ProximityAnalytics proximityAnalytics;

    Analytics(FirebaseAnalytics firebaseAnalytics, Crashlytics crashlytics, ProximityAnalytics proximityAnalitics) {
        this.firebaseAnalytics = firebaseAnalytics;
        this.crashlytics = crashlytics;
        this.proximityAnalytics = proximityAnalitics;
    }

    public void initializeStaticUserProperties() {
        firebaseAnalytics.setUserProperty("debug_build", String.valueOf(BuildConfig.DEBUG));
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
        viewEvent.putContentName(screenName.toLowerCase());
        viewEvent.putContentId(screenName);
        viewEvent.putContentType("screen");
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

    public void trackProximityEventShown(ProximityEvent event) {
        proximityAnalytics.trackProximityEvent(event, ProximityTrackingType.NOTIFIED);
    }

    public void trackProximityEventEngaged(ProximityEvent event) {
        proximityAnalytics.trackProximityEvent(event, ProximityTrackingType.ENGAGED);
    }

    public void enableExceptionLogging() {
        Timber.plant(new CrashlyticsErrorsTree());
    }
}
