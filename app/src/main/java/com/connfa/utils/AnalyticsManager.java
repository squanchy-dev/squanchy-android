package com.connfa.utils;

import android.app.Activity;

import com.connfa.ConnfaApplication;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class AnalyticsManager {

    public static void sendEvent(Activity activity, String category, int actionId) {
        Tracker t = ((ConnfaApplication) activity.getApplication()).getTracker();
        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(activity.getString(actionId))
                .setLabel(null)
                .build());
    }

    public static void sendEvent(Activity activity, int categoryId, int actionId, long id) {
        Tracker t = ((ConnfaApplication) activity.getApplication()).getTracker();
        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
                .setCategory(activity.getString(categoryId))
                .setAction(activity.getString(actionId))
                .setLabel(Long.toString(id))
                .build());
    }

    public static void sendEvent(Activity activity, int categoryId, int actionId, String title) {
        Tracker t = ((ConnfaApplication) activity.getApplication()).getTracker();
        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
                .setCategory(activity.getString(categoryId))
                .setAction(activity.getString(actionId))
                .setLabel(title)
                .build());
    }
}
