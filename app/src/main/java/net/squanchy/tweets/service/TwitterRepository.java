package net.squanchy.tweets.service;

import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.services.SearchService;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;

public class TwitterRepository {

    private static final int MAX_ITEM_PER_REQUEST = 100;

    private final SearchService searchService = TwitterCore.getInstance().getApiClient().getSearchService();

    Single<Search> load(String query) {
        return Single.fromCallable(() -> createSearchRequest(query).execute().body())
                .subscribeOn(Schedulers.io());
    }

    private Call<Search> createSearchRequest(String query) throws Exception {
        return searchService.tweets(query, null, null, null, "recent", MAX_ITEM_PER_REQUEST, null, null, null, true);
    }
}
