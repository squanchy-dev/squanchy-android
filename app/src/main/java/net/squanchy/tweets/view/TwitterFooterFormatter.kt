package net.squanchy.tweets.view

import android.content.Context
import androidx.annotation.StringRes
import com.google.firebase.Timestamp
import net.squanchy.R
import net.squanchy.tweets.domain.view.TweetViewModel
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
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
        val createdAt = tweet.createdAt.toLocalDateTime().toDateTime()
        val formattedTime = timeFormatter.print(tweet.createdAt.toDate().time)

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
}

private fun Timestamp.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.fromDateFields(this.toDate())
}
