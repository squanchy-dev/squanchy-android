package net.squanchy.schedule;

import android.content.Context;
import android.content.Intent;

import net.squanchy.eventdetails.EventDetailsActivity;
import net.squanchy.navigation.Navigator;

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
    public void toEventDetails(int dayId, int eventId) {
        Intent intent = EventDetailsActivity.createIntent(context, dayId, eventId);
        context.startActivity(intent);
    }

    @Override
    public void toSchedule() {

    }

    @Override
    public void toFavorites() {

    }

    @Override
    public void toSpeakers() {

    }
}
