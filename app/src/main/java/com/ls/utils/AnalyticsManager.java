package com.ls.utils;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import com.ls.drupalcon.app.App;

import android.app.Activity;

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
}
