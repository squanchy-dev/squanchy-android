package net.squanchy.navigation;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

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

    public void toTwitterProfile(String username) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + username));
        if (canResolve(intent)) {
            context.startActivity(intent);
        } else {
            toExternalUrl("https://twitter.com/" + username);
            context.startActivity(intent);
        }
    }

    private boolean canResolve(Intent intent) {
        return !context.getPackageManager()
                .queryIntentActivities(intent, 0)
                .isEmpty();
    }

    public void toExternalUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }
}
