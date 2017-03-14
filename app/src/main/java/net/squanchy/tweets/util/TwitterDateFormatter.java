package net.squanchy.tweets.util;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateUtils;

import com.twitter.sdk.android.core.models.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import net.squanchy.R;
import net.squanchy.support.lang.Optional;

public class TwitterDateFormatter {

    // Sat Mar 14 02:34:20 +0000 2009
    private static final SimpleDateFormat DATE_TIME_RFC822 = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
    private static final SimpleDateFormat RELATIVE_DATE_FORMAT = new SimpleDateFormat("MM/dd/yy", Locale.ENGLISH);
    private static final long INVALID_DATE = -1;

    private TwitterDateFormatter() {
    }

    public static String getTimestampFrom(Tweet tweet, Context context) {
        return getOptionalTimestamp(tweet.createdAt, context)
                .or(context.getString(R.string.tweet_date_not_available));
    }

    private static Optional<String> getOptionalTimestamp(String date, Context context) {

        if (date != null && TwitterDateFormatter.isValidTimestamp(date)) {
            return Optional.absent();
        }
        final Long createdAtTimestamp = TwitterDateFormatter.apiTimeToLong(date);
        String formattedTimestamp = TwitterDateFormatter.getRelativeTimeString(
                context.getResources(),
                System.currentTimeMillis(),
                createdAtTimestamp);

        return Optional.of(formattedTimestamp);
    }

    private static long apiTimeToLong(String apiTime) {
        if (apiTime == null) {
            return INVALID_DATE;
        }

        try {
            return DATE_TIME_RFC822.parse(apiTime).getTime();
        } catch (ParseException e) {
            return INVALID_DATE;
        }
    }

    private static boolean isValidTimestamp(String timestamp) {
        return apiTimeToLong(timestamp) != INVALID_DATE;
    }

    /**
     * This method is not thread safe. It has been modified from the original to not rely on global
     * time state. If a timestamp is in the future we return it as an absolute date string. Within
     * the same second we return 0s
     *
     * @param res               resource
     * @param currentTimeMillis timestamp for offset
     * @param timestamp         timestamp
     * @return the relative time string
     */
    private static String getRelativeTimeString(Resources res, long currentTimeMillis, long timestamp) {
        final long diff = currentTimeMillis - timestamp;
        if (diff >= 0) {
            if (diff < DateUtils.MINUTE_IN_MILLIS) { // Less than a minute ago
                final int secs = (int) (diff / 1000);
                return res.getQuantityString(com.twitter.sdk.android.tweetui.R.plurals.tw__time_secs, secs, secs);
            } else if (diff < DateUtils.HOUR_IN_MILLIS) { // Less than an hour ago
                final int mins = (int) (diff / DateUtils.MINUTE_IN_MILLIS);
                return res.getQuantityString(com.twitter.sdk.android.tweetui.R.plurals.tw__time_mins, mins, mins);
            } else if (diff < DateUtils.DAY_IN_MILLIS) { // Less than a day ago
                final int hours = (int) (diff / DateUtils.HOUR_IN_MILLIS);
                return res.getQuantityString(com.twitter.sdk.android.tweetui.R.plurals.tw__time_hours, hours, hours);
            } else {
                final Calendar now = Calendar.getInstance();
                now.setTimeInMillis(currentTimeMillis);
                final Calendar c = Calendar.getInstance();
                c.setTimeInMillis(timestamp);
                final Date d = new Date(timestamp);

                if (now.get(Calendar.YEAR) == c.get(Calendar.YEAR)) {
                    // Same year
                    RELATIVE_DATE_FORMAT.applyPattern(res.getString(R.string.tw__relative_date_format_short));
                } else {
                    // Outside of our year
                    RELATIVE_DATE_FORMAT.applyPattern(res.getString(R.string.tw__relative_date_format_long));
                }
                return RELATIVE_DATE_FORMAT.format(d);
            }
        }
        RELATIVE_DATE_FORMAT.applyPattern(res.getString(R.string.tw__relative_date_format_long));
        return RELATIVE_DATE_FORMAT.format(new Date(timestamp));
    }
}
