package com.connfa.utils;

import android.app.Activity;

import com.connfa.App;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class AnalyticsManager {

    public static void sendEvent(Activity activity, String category, int actionId) {
        Tracker t = ((App) activity.getApplication()).getTracker();
        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(activity.getString(actionId))
                .setLabel(null)
                .build());
    }

    public static void sendEvent(Activity activity, int categoryId, int actionId, long id) {
        Tracker t = ((App) activity.getApplication()).getTracker();
        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
                .setCategory(activity.getString(categoryId))
                .setAction(activity.getString(actionId))
                .setLabel(Long.toString(id))
                .build());
    }

    public static void sendEvent(Activity activity, int categoryId, int actionId, String title) {
        Tracker t = ((App) activity.getApplication()).getTracker();
        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
                .setCategory(activity.getString(categoryId))
                .setAction(activity.getString(actionId))
                .setLabel(title)
                .build());
    }
}
