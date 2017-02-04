package com.connfa.schedule;

import android.content.Context;
import android.content.Intent;

import com.connfa.eventdetails.EventDetailsActivity;
import com.connfa.navigation.Navigator;

class ScheduleNavigator implements Navigator {

    private final Context context;

    ScheduleNavigator(Context context) {
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
