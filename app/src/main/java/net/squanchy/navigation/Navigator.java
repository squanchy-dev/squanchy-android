package net.squanchy.navigation;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import net.squanchy.BuildConfig;
import net.squanchy.about.AboutActivity;
import net.squanchy.eventdetails.EventDetailsActivity;
import net.squanchy.home.HomeActivity;
import net.squanchy.search.SearchActivity;
import net.squanchy.settings.SettingsActivity;
import net.squanchy.signin.SignInActivity;
import net.squanchy.speaker.SpeakerDetailsActivity;
import net.squanchy.support.lang.Optional;
import net.squanchy.venue.domain.view.Venue;

import timber.log.Timber;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

public class Navigator {

    private final Context context;
    private final DebugActivityIntentFactory debugActivityIntentFactory;

    Navigator(Context context, DebugActivityIntentFactory debugActivityIntentFactory) {
        this.context = context;
        this.debugActivityIntentFactory = debugActivityIntentFactory;
    }

    public void toEventDetails(String eventId) {
        start(EventDetailsActivity.createIntent(context, eventId));
    }

    public void toSignIn() {
        start(new Intent(context, SignInActivity.class));
    }

    public void toSpeakerDetails(String speakerId) {
        start(
                SpeakerDetailsActivity.createIntent(context, speakerId),
                FLAG_ACTIVITY_SINGLE_TOP | FLAG_ACTIVITY_CLEAR_TOP
        );
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

    public void toMapsFor(Venue venue) {
        String mapsUrl = "http://maps.google.com/?daddr=" + Uri.encode(venue.name()) + "," + Uri.encode(venue.address());
        toExternalUrl(mapsUrl);
    }

    public void toExternalUrl(String url) {
        start(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public void toSchedule() {
        toSchedule(Optional.absent(), Optional.absent());
    }

    public void toSchedule(Optional<String> dayId, Optional<String> eventId) {
        start(
                HomeActivity.createScheduleIntent(context, dayId, eventId),
                FLAG_ACTIVITY_SINGLE_TOP | FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK
        );
    }

    public void toFavorites() {
        start(
                HomeActivity.createFavoritesIntent(context),
                FLAG_ACTIVITY_SINGLE_TOP | FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK
        );
    }

    public void toTwitterFeed() {
        start(
                HomeActivity.createTweetsIntent(context),
                FLAG_ACTIVITY_SINGLE_TOP | FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK
        );
    }

    public void toVenueInfo() {
        start(
                HomeActivity.createVenueInfoIntent(context),
                FLAG_ACTIVITY_SINGLE_TOP | FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK
        );
    }

    public void toDebugSettings() {
        if (BuildConfig.DEBUG) {
            start(debugActivityIntentFactory.createDebugActivityIntent(context));
        } else {
            Timber.e("Someone is trying to reach the debug activity in a release build... that won't work");
        }
    }

    public void toAboutSquanchy() {
        start(new Intent(context, AboutActivity.class));
    }

    private void start(Intent intent) {
        start(intent, 0);
    }

    private void start(Intent intent, int flags) {
        if (flags != 0) {
            intent.addFlags(flags);
        }
        context.startActivity(intent);
    }
}
