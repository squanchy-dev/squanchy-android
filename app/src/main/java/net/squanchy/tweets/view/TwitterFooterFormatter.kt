package net.squanchy.tweets.view

import android.content.Context
import androidx.annotation.StringRes
import net.squanchy.R
import net.squanchy.support.time.createShortDateFormatter
import net.squanchy.support.time.createShortTimeFormatter
import net.squanchy.support.time.toZonedDateTime
import net.squanchy.tweets.domain.view.TweetViewModel
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime

internal class TwitterFooterFormatter(private val context: Context) {

    private val timeFormatter = createShortTimeFormatter()
    private val dateFormatter = createShortDateFormatter()

    fun footerTextFor(tweet: TweetViewModel): String {
        val username = tweet.user.screenName
        val timestamp = timestampFrom(tweet)
        return context.getString(R.string.tweet_footer_format, username, timestamp)
    }

    private fun timestampFrom(tweet: TweetViewModel): String {
        val createdAt = tweet.createdAt.toZonedDateTime(ZoneId.systemDefault())
        val formattedTime = timeFormatter.format(createdAt)

        return when {
            isToday(createdAt) -> formatRecentDay(context, R.string.tweet_date_today, formattedTime)
            isYesterday(createdAt) -> formatRecentDay(context, R.string.tweet_date_yesterday, formattedTime)
            else -> formatDateAndTime(createdAt, formattedTime)
        }
    }

    private fun isToday(instant: ZonedDateTime): Boolean {
        return instant.toLocalDate() == LocalDate.now(instant.zone)
    }

    private fun isYesterday(instant: ZonedDateTime): Boolean {
        return instant.toLocalDate() == LocalDate.now(instant.zone).minusDays(1)
    }

    private fun formatRecentDay(context: Context, @StringRes dayRes: Int, formattedTime: String): String {
        val day = context.getString(dayRes)
        return context.getString(R.string.tweet_date_format, day, formattedTime)
    }

    private fun formatDateAndTime(date: ZonedDateTime, formattedTimestamp: String): String {
        val formattedDate = dateFormatter.format(date)
        return formatNormalDay(context, formattedDate, formattedTimestamp)
    }

    private fun formatNormalDay(context: Context, day: String, formattedTime: String) =
        context.getString(R.string.tweet_date_format, day, formattedTime)
}
