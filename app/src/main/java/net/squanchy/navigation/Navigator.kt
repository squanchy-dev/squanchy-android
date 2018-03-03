package net.squanchy.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.net.Uri
import net.squanchy.BuildConfig
import net.squanchy.about.AboutActivity
import net.squanchy.about.licenses.LicensesActivity
import net.squanchy.eventdetails.EventDetailsActivity
import net.squanchy.home.HomeActivity
import net.squanchy.navigation.firststart.FirstStartWithNoNetworkActivity
import net.squanchy.onboarding.OnboardingPage
import net.squanchy.schedule.tracksfilter.FilterScheduleActivity
import net.squanchy.search.SearchActivity
import net.squanchy.settings.SettingsActivity
import net.squanchy.signin.SignInActivity
import net.squanchy.speaker.SpeakerDetailsActivity
import net.squanchy.tweets.domain.TweetLinkInfo
import net.squanchy.venue.domain.view.Venue
import timber.log.Timber

class Navigator(
        private val activity: Activity,
        private val debugActivityIntentFactory: DebugActivityIntentFactory
) {

    fun toEventDetails(eventId: String) {
        start(EventDetailsActivity.createIntent(activity, eventId))
    }

    fun toSpeakerDetails(speakerId: String) {
        start(
                SpeakerDetailsActivity.createIntent(activity, speakerId),
                FLAG_ACTIVITY_SINGLE_TOP or FLAG_ACTIVITY_CLEAR_TOP
        )
    }

    fun toSearch() {
        start(Intent(activity, SearchActivity::class.java))
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    fun toSettings() {
        start(Intent(activity, SettingsActivity::class.java))
    }

    fun toTwitterProfile(username: String) {
        val deeplinkProfileUrl = "twitter://user?screen_name=$username"
        val fallbackProfileUrl = "https://twitter.com/$username"
        attemptDeeplinkOrFallback(deeplinkProfileUrl, fallbackProfileUrl)
    }

    fun toTweet(linkInfo: TweetLinkInfo) {
        val deeplinkStatusUrl = "twitter://status?status_id=${linkInfo.statusId}"
        val fallbackStatusUrl = "http://twitter.com/${linkInfo.screenName}/status/${linkInfo.statusId}"
        attemptDeeplinkOrFallback(deeplinkStatusUrl, fallbackStatusUrl)
    }

    fun toScheduleFiltering(ctx: Context) {
        start(Intent(ctx, FilterScheduleActivity::class.java))
    }

    private fun attemptDeeplinkOrFallback(deeplinkUrl: String, fallbackUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(deeplinkUrl))
        if (canResolve(intent)) {
            start(intent)
        } else {
            toExternalUrl(fallbackUrl)
        }
    }

    private fun canResolve(intent: Intent) =
        !activity.packageManager
            .queryIntentActivities(intent, 0)
            .isEmpty()

    fun toMapsFor(venue: Venue) {
        toExternalUrl("http://maps.google.com/?daddr=${Uri.encode(venue.name)},${Uri.encode(venue.address)}")
    }

    fun toExternalUrl(url: String) {
        start(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    internal fun toHomePage() {
        toSchedule(null, null)
    }

    fun toSchedule(dayId: String?, eventId: String?) {
        start(
                HomeActivity.createScheduleIntent(activity, dayId, eventId),
                FLAG_ACTIVITY_SINGLE_TOP or FLAG_ACTIVITY_CLEAR_TOP or FLAG_ACTIVITY_CLEAR_TASK or FLAG_ACTIVITY_NEW_TASK
        )
    }

    fun toFavorites() {
        start(
                HomeActivity.createFavoritesIntent(activity),
                FLAG_ACTIVITY_SINGLE_TOP or FLAG_ACTIVITY_CLEAR_TOP or FLAG_ACTIVITY_CLEAR_TASK or FLAG_ACTIVITY_NEW_TASK
        )
    }

    fun toTwitterFeed() {
        start(
                HomeActivity.createTweetsIntent(activity),
                FLAG_ACTIVITY_SINGLE_TOP or FLAG_ACTIVITY_CLEAR_TOP or FLAG_ACTIVITY_CLEAR_TASK or FLAG_ACTIVITY_NEW_TASK
        )
    }

    fun toVenueInfo() {
        start(
                HomeActivity.createVenueInfoIntent(activity),
                FLAG_ACTIVITY_SINGLE_TOP or FLAG_ACTIVITY_CLEAR_TOP or FLAG_ACTIVITY_CLEAR_TASK or FLAG_ACTIVITY_NEW_TASK
        )
    }

    fun toDebugSettings() {
        if (BuildConfig.DEBUG) {
            start(debugActivityIntentFactory.createDebugActivityIntent(activity))
        } else {
            Timber.e("Someone is trying to reach the debug activity in a release build... that won't work")
        }
    }

    fun toAboutSquanchy() {
        start(Intent(activity, AboutActivity::class.java))
    }

    fun toFossLicenses() {
        start(Intent(activity, LicensesActivity::class.java))
    }

    fun toSignIn() {
        start(Intent(activity, SignInActivity::class.java))
    }

    internal fun toFirstStartWithNoNetwork(continuationIntent: Intent) {
        start(
                FirstStartWithNoNetworkActivity.createIntentContinuingTo(activity, continuationIntent),
                FLAG_ACTIVITY_CLEAR_TASK or FLAG_ACTIVITY_NEW_TASK
        )
    }

    private fun start(intent: Intent, flags: Int = NO_FLAGS) {
        if (flags != NO_FLAGS) {
            intent.addFlags(flags)
        }
        activity.startActivity(intent)
    }

    fun toSignInForResult(requestCode: Int) {
        val intent = Intent(activity, SignInActivity::class.java)
        startForResult(intent, requestCode)
    }

    fun toOnboardingForResult(page: OnboardingPage, requestCode: Int) {
        val intent = Intent(activity, page.activityClass)
        startForResult(intent, requestCode)
    }

    private fun startForResult(intent: Intent, requestCode: Int) {
        activity.startActivityForResult(intent, requestCode)
    }

    companion object {

        private const val NO_FLAGS = 0
    }
}
