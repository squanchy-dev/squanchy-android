package net.squanchy.tweets.model;

import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.tweetui.Timeline;

import java.text.SimpleDateFormat;
import java.util.Locale;

import io.reactivex.Observable;

public class SearchTimeline {

    static final String FILTER_RETWEETS = " -filter:retweets";
    private static final String SCRIBE_SECTION = "search";
    private static final SimpleDateFormat QUERY_DATE = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    final String query;
    final String resultType;
    final Integer maxItemsPerRequest;

    SearchTimeline(String query, String resultType, Integer maxItemsPerRequest) {
        this.query = query + FILTER_RETWEETS;
        this.maxItemsPerRequest = maxItemsPerRequest;
        this.resultType = resultType;
    }


    public void next(Long sinceId) {
        //next
    }

    public void previous(Long maxId) {
       //previous
    }

    Observable<Search> createSearchRequest(final Long sinceId, final Long maxId) {
        return null;
    }

    public enum ResultType {
        RECENT("recent"),
        POPULAR("popular"),
        MIXED("mixed"),
        FILTERED("filtered");

        final String type;

        ResultType(String type) {
            this.type = type;
        }
    }
}

