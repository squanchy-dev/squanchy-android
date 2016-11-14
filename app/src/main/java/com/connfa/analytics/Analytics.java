package com.connfa.analytics;

import android.app.Application;
import android.content.Context;

import com.connfa.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders.EventBuilder;
import com.google.android.gms.analytics.Tracker;

import java.util.Map;

public class Analytics {

    private final Tracker tracker;

    public static Analytics from(Context context) {
        Application application = (Application) context.getApplicationContext();
        Tracker tracker = GoogleAnalytics.getInstance(application)
                .newTracker(R.xml.global_tracker);
        return new Analytics(tracker);
    }

    private Analytics(Tracker tracker) {
        this.tracker = tracker;
    }

    public void sendEvent(String category, String action) {
        Map<String, String> event = new EventBuilder(category, action).build();
        tracker.send(event);
    }

    public void sendEvent(String label, String category, String action) {
        Map<String, String> eventMap = new EventBuilder(category, action)
                .setLabel(label)
                .build();
        tracker.send(eventMap);
    }

    public void enableActivityLifecycleLogging() {
        tracker.enableAutoActivityTracking(true);
    }

    public void enableExceptionLogging() {
        tracker.enableExceptionReporting(true);
    }
}
