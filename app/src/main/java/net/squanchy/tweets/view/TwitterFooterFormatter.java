package net.squanchy.tweets.view;

import android.content.Context;
import android.support.annotation.StringRes;

import java.util.Locale;

import net.squanchy.R;
import net.squanchy.tweets.domain.view.TweetViewModel;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

class TwitterFooterFormatter {

    // Sat Mar 14 02:34:20 +0000 2009
    private static final String DATE_PATTERN = "EEE MMM dd HH:mm:ss Z yyyy";

    private final Context context;
    private final DateTimeFormatter timeFormatter;
    private final DateTimeFormatter dateFormatter;

    TwitterFooterFormatter(Context context) {
        this.context = context;
        this.timeFormatter = DateTimeFormat.shortTime();
        this.dateFormatter = DateTimeFormat.shortDate();
    }

    String footerTextFor(TweetViewModel tweet) {
        String username = tweet.getUser().getScreenName();
        String timestamp = timestampFrom(tweet);
        return context.getString(R.string.tweet_footer_format, username, timestamp);
    }

    private String timestampFrom(TweetViewModel tweet) {
        DateTime createdAt = DateTimeFormat.forPattern(DATE_PATTERN)
                .withLocale(Locale.US)
                .parseDateTime(tweet.getCreatedAt());

        String formattedTime = timeFormatter.print(createdAt);

        if (isToday(createdAt)) {
            return formatRecentDay(context, R.string.tweet_date_today, formattedTime);
        } else if (isYesterday(createdAt)) {
            return formatRecentDay(context, R.string.tweet_date_yesterday, formattedTime);
        } else {
            return formatDateAndTime(createdAt, formattedTime);
        }
    }

    private boolean isToday(DateTime instant) {
        DateTime today = new DateTime().withTimeAtStartOfDay();
        return today.isEqual(instant.withTimeAtStartOfDay());
    }

    private boolean isYesterday(DateTime instant) {
        DateTime today = new DateTime().minusDays(1).withTimeAtStartOfDay();
        return today.isEqual(instant.withTimeAtStartOfDay());
    }

    private String formatRecentDay(Context context, @StringRes int dayRes, String formattedTime) {
        String day = context.getString(dayRes);
        return context.getString(R.string.tweet_date_format, day, formattedTime);
    }

    private String formatDateAndTime(DateTime date, String formattedTimestamp) {
        String formattedDate = dateFormatter.print(date);
        return formatNormalDay(context, formattedDate, formattedTimestamp);
    }

    private String formatNormalDay(Context context, String day, String formattedTime) {
        return context.getString(R.string.tweet_date_format, day, formattedTime);
    }
}
