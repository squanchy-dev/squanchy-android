package net.squanchy.tweets.model;

import com.twitter.sdk.android.core.models.Search;

import io.reactivex.Observable;

public class SearchTimeline {

    private static final String FILTER_RETWEETS = " -filter:retweets";

    private boolean isLoading = false;

    private final String query;
    private final String resultType;
    private final Integer maxItemsPerRequest;

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
        isLoading = true;
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

