package com.connfa.schedule.navigation;

import android.content.Context;
import android.content.Intent;

import com.connfa.eventdetails.EventDetailsActivity;
import com.connfa.navigation.Navigator;

public class ScheduleActivityNavigator implements Navigator {

    private final Context context;

    public ScheduleActivityNavigator(Context context) {
        this.context = context;
    }

    @Override
    public void up() {
        // No-op (top level yo)
    }

    @Override
    public void toEventDetails() {
        context.startActivity(new Intent(context, EventDetailsActivity.class));
    }
}
