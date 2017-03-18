package net.squanchy.navigation;

import android.content.Context;
import android.content.Intent;

import net.squanchy.eventdetails.EventDetailsActivity;
import net.squanchy.search.SearchActivity;
import net.squanchy.speaker.SpeakerDetailsActivity;

public class Navigator {

    private final Context context;

    public Navigator(Context context) {
        this.context = context;
    }

    public void toEventDetails(String eventId) {
        Intent intent = EventDetailsActivity.createIntent(context, eventId);
        context.startActivity(intent);
    }

    public void toSpeakerDetails(String speakerId) {
        Intent intent = SpeakerDetailsActivity.createIntent(context, speakerId);
        context.startActivity(intent);
    }

    public void toSearch() {
        context.startActivity(new Intent(context, SearchActivity.class));
    }
}
