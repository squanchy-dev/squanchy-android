package net.squanchy.navigation;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import net.squanchy.BuildConfig;
import net.squanchy.about.AboutActivity;
import net.squanchy.about.licenses.LicensesActivity;
import net.squanchy.contest.ContestActivity;
import net.squanchy.eventdetails.EventDetailsActivity;
import net.squanchy.home.HomeActivity;
import net.squanchy.onboarding.OnboardingPage;
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

    private static final int NO_FLAGS = 0;
    private final Activity activity;
    private final DebugActivityIntentFactory debugActivityIntentFactory;

    Navigator(Activity activity, DebugActivityIntentFactory debugActivityIntentFactory) {
        this.activity = activity;
        this.debugActivityIntentFactory = debugActivityIntentFactory;
    }

    public void toEventDetails(String eventId) {
        start(EventDetailsActivity.createIntent(activity, eventId));
    }

    public void toSpeakerDetails(String speakerId) {
        start(
                SpeakerDetailsActivity.createIntent(activity, speakerId),
                FLAG_ACTIVITY_SINGLE_TOP | FLAG_ACTIVITY_CLEAR_TOP
        );
    }

    public void toSearch() {
        start(new Intent(activity, SearchActivity.class));
    }

    public void toContest() {
        Intent intent = ContestActivity.createIntent(activity);
        start(
                intent,
                FLAG_ACTIVITY_SINGLE_TOP
        );
    }

    public void toContest(String achievementId) {
        Intent intent = ContestActivity.createIntent(activity, achievementId);
        start(
                intent,
                FLAG_ACTIVITY_SINGLE_TOP
        );
    }

    public void toSettings() {
        start(new Intent(activity, SettingsActivity.class));
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
        return !activity.getPackageManager()
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
                HomeActivity.createScheduleIntent(activity, dayId, eventId),
                FLAG_ACTIVITY_SINGLE_TOP | FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK
        );
    }

    public void toFavorites() {
        start(
                HomeActivity.createFavoritesIntent(activity),
                FLAG_ACTIVITY_SINGLE_TOP | FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK
        );
    }

    public void toTwitterFeed() {
        start(
                HomeActivity.createTweetsIntent(activity),
                FLAG_ACTIVITY_SINGLE_TOP | FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK
        );
    }

    public void toVenueInfo() {
        start(
                HomeActivity.createVenueInfoIntent(activity),
                FLAG_ACTIVITY_SINGLE_TOP | FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK
        );
    }

    public void toDebugSettings() {
        if (BuildConfig.DEBUG) {
            start(debugActivityIntentFactory.createDebugActivityIntent(activity));
        } else {
            Timber.e("Someone is trying to reach the debug activity in a release build... that won't work");
        }
    }

    public void toAboutSquanchy() {
        start(new Intent(activity, AboutActivity.class));
    }

    public void toFossLicenses() {
        start(new Intent(activity, LicensesActivity.class));
    }

    public void toSignIn() {
        start(new Intent(activity, SignInActivity.class));
    }

    private void start(Intent intent) {
        start(intent, NO_FLAGS);
    }

    private void start(Intent intent, int flags) {
        if (flags != NO_FLAGS) {
            intent.addFlags(flags);
        }
        activity.startActivity(intent);
    }

    public void toOnboardingForResult(OnboardingPage page, int requestCode) {
        Intent intent = new Intent(activity, page.activityClass());
        startForResult(intent, requestCode);
    }

    private void startForResult(Intent intent, int requestCode) {
        activity.startActivityForResult(intent, requestCode);
    }
}
