package net.squanchy.schedule;

import android.content.Context;
import android.content.Intent;

import net.squanchy.eventdetails.EventDetailsActivity;
import net.squanchy.navigation.Navigator;
import net.squanchy.search.SearchActivity;

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
    public void toEventDetails(String eventId) {
        Intent intent = EventDetailsActivity.createIntent(context, eventId);
        context.startActivity(intent);
    }

    @Override
    public void toSearch() {
        context.startActivity(new Intent(context, SearchActivity.class));
    }
}
