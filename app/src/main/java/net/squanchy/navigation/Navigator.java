package net.squanchy.navigation;

import android.content.Context;
import android.content.Intent;

import net.squanchy.eventdetails.EventDetailsActivity;
import net.squanchy.search.SearchActivity;

public class Navigator {

    private final Context context;

    public Navigator(Context context) {
        this.context = context;
    }

    public void up() {
        // No-op (top level yo)
    }

    public void toEventDetails(String eventId) {
        Intent intent = EventDetailsActivity.createIntent(context, eventId);
        context.startActivity(intent);
    }

    public void toSearch() {
        context.startActivity(new Intent(context, SearchActivity.class));
    }
}
