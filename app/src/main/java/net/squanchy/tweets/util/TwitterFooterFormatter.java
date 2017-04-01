package net.squanchy.tweets.util;

import android.content.Context;
import android.support.annotation.StringRes;

import java.util.Locale;

import net.squanchy.R;
import net.squanchy.tweets.domain.view.TweetViewModel;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TwitterFooterFormatter {

    // Sat Mar 14 02:34:20 +0000 2009
    private static final String DATE_PATTERN = "EEE MMM dd HH:mm:ss Z yyyy";
    private static final String AT = "@";
    private static final String EM_DASH = "—";

    private TwitterFooterFormatter() {
    }

    public static String recapFrom(TweetViewModel tweet, Context context){
        return new StringBuilder()
                .append(AT)
                .append(tweet.user().screenName())
                .append(EM_DASH)
                .append(timestampFrom(tweet, context))
                .toString();
    }

    private static String timestampFrom(TweetViewModel tweet, Context context) {

        DateTime dateTime = DateTimeFormat.forPattern(DATE_PATTERN).withLocale(Locale.US).parseDateTime(tweet.createdAt());
        String time = DateTimeFormat.shortTime().print(dateTime);

        if (isToday(dateTime)) {
            return formatRecentDay(context, R.string.tweet_date_today, time);
        }

        if (isYesterday(dateTime)) {
            return formatRecentDay(context, R.string.tweet_date_yesterday, time);
        }

        DateTimeFormatter dateFormatter = DateTimeFormat.shortDate();
        return formatNormalDay(context, dateFormatter.print(dateTime), time);
    }

    private static boolean isToday(DateTime dateTime) {
        DateTime today = new DateTime().withTimeAtStartOfDay();
        return today.isEqual(dateTime.withTimeAtStartOfDay());
    }

    private static boolean isYesterday(DateTime dateTime) {
        DateTime yesterday = new DateTime().minusDays(1).withTimeAtStartOfDay();
        return yesterday.isEqual(dateTime.withTimeAtStartOfDay());
    }

    private static String formatRecentDay(Context context, @StringRes int dayRes, String formattedTime) {
        String day = context.getString(dayRes);
        return context.getString(R.string.tweet_date_recent_format, day, formattedTime);
    }

    private static String formatNormalDay(Context context, String day, String formattedTime) {
        return context.getString(R.string.tweet_date_recent_format, day, formattedTime);
    }
}
