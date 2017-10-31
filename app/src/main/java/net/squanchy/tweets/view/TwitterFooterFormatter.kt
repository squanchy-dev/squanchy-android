package net.squanchy.tweets.view

import android.content.Context
import android.support.annotation.StringRes

import java.util.Locale

import net.squanchy.R
import net.squanchy.tweets.domain.view.TweetViewModel

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

internal class TwitterFooterFormatter(private val context: Context) {

    private val timeFormatter: DateTimeFormatter = DateTimeFormat.shortTime()
    private val dateFormatter: DateTimeFormatter = DateTimeFormat.shortDate()

    fun footerTextFor(tweet: TweetViewModel): String {
        val username = tweet.user.screenName
        val timestamp = timestampFrom(tweet)
        return context.getString(R.string.tweet_footer_format, username, timestamp)
    }

    private fun timestampFrom(tweet: TweetViewModel): String {
        val createdAt = DateTimeFormat.forPattern(DATE_PATTERN)
            .withLocale(Locale.US)
            .parseDateTime(tweet.createdAt)

        val formattedTime = timeFormatter.print(createdAt)

        return when {
            isToday(createdAt) -> formatRecentDay(context, R.string.tweet_date_today, formattedTime)
            isYesterday(createdAt) -> formatRecentDay(context, R.string.tweet_date_yesterday, formattedTime)
            else -> formatDateAndTime(createdAt, formattedTime)
        }
    }

    private fun isToday(instant: DateTime): Boolean {
        val today = DateTime().withTimeAtStartOfDay()
        return today.isEqual(instant.withTimeAtStartOfDay())
    }

    private fun isYesterday(instant: DateTime): Boolean {
        val today = DateTime().minusDays(1).withTimeAtStartOfDay()
        return today.isEqual(instant.withTimeAtStartOfDay())
    }

    private fun formatRecentDay(context: Context, @StringRes dayRes: Int, formattedTime: String): String {
        val day = context.getString(dayRes)
        return context.getString(R.string.tweet_date_format, day, formattedTime)
    }

    private fun formatDateAndTime(date: DateTime, formattedTimestamp: String): String {
        val formattedDate = dateFormatter.print(date)
        return formatNormalDay(context, formattedDate, formattedTimestamp)
    }

    private fun formatNormalDay(context: Context, day: String, formattedTime: String) =
        context.getString(R.string.tweet_date_format, day, formattedTime)

    companion object {

        // Sat Mar 14 02:34:20 +0000 2009
        private const val DATE_PATTERN = "EEE MMM dd HH:mm:ss Z yyyy"
    }
}
