package net.squanchy.navigation;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import net.squanchy.eventdetails.EventDetailsActivity;
import net.squanchy.search.SearchActivity;
import net.squanchy.settings.SettingsActivity;

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

    public void toSpeakerDetails(String... speakerIds) {
        // TODO open the speaker detail view here
        Toast.makeText(context, "Speakers clicked: " + TextUtils.join(", ", speakerIds), Toast.LENGTH_SHORT).show();
    }

    public void toSearch() {
        context.startActivity(new Intent(context, SearchActivity.class));
    }

    public void toSettings() {
        context.startActivity(new Intent(context, SettingsActivity.class));
    }
}
