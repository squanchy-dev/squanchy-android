package net.squanchy.navigation;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import net.squanchy.BuildConfig;
import net.squanchy.DebugActivity;
import net.squanchy.eventdetails.EventDetailsActivity;
import net.squanchy.home.HomeActivity;
import net.squanchy.search.SearchActivity;
import net.squanchy.settings.SettingsActivity;
import net.squanchy.signin.SignInActivity;
import net.squanchy.speaker.SpeakerDetailsActivity;
import net.squanchy.support.lang.Optional;

import timber.log.Timber;

public class Navigator {

    private final Context context;

    public Navigator(Context context) {
        this.context = context;
    }

    public void toEventDetails(String eventId) {
        start(EventDetailsActivity.createIntent(context, eventId));
    }

    public void toSignIn() {
        start(new Intent(context, SignInActivity.class));
    }

    public void toSpeakerDetails(String speakerId) {
        start(SpeakerDetailsActivity.createIntent(context, speakerId));
    }

    public void toSearch() {
        start(new Intent(context, SearchActivity.class));
    }

    public void toSettings() {
        start(new Intent(context, SettingsActivity.class));
    }

    public void toTwitterProfile(String username) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + username));
        if (canResolve(intent)) {
            start(intent);
        } else {
            toExternalUrl("https://twitter.com/" + username);
        }
    }

    private boolean canResolve(Intent intent) {
        return !context.getPackageManager()
                .queryIntentActivities(intent, 0)
                .isEmpty();
    }

    public void toExternalUrl(String url) {
        start(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public void toSchedule(Optional<String> dayId, Optional<String> eventId) {
        start(HomeActivity.createScheduleIntent(context, dayId, eventId));
    }

    public void toFavorites() {
        start(HomeActivity.createFavoritesIntent(context));
    }

    public void toTwitterFeed() {
        start(HomeActivity.createTweetsIntent(context));
    }

    public void toVenueInfo() {
        start(HomeActivity.createVenueInfoIntent(context));
    }

    public void toDebugSettings() {
        if (BuildConfig.DEBUG) {
            start(new Intent(context, DebugActivity.class));
        } else {
            Timber.e("Someone is trying to reach the debug activity in a release build... that won't work");
        }
    }

    private void start(Intent intent) {
        context.startActivity(intent);
    }
}
