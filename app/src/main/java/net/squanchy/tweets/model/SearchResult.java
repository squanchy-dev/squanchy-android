package net.squanchy.tweets.model;

import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.core.models.SearchMetadata;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

public class SearchResult {

    @SerializedName("statuses")
    public final List<Tweet> tweets;
    @SerializedName("search_metadata")
    public final SearchMetadata searchMetadata;

    public SearchResult(List<Tweet> tweets, SearchMetadata searchMetadata) {
        this.tweets = tweets;
        this.searchMetadata = searchMetadata;
    }
}
