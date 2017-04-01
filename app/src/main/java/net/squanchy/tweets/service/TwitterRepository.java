package net.squanchy.tweets.service;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.services.SearchService;

import java.net.SocketTimeoutException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import retrofit2.Call;

public class TwitterRepository {

    private static final int MAX_ITEM_PER_REQUEST = 100;

    private final SearchService searchService = TwitterCore.getInstance().getApiClient().getSearchService();

    Observable<Search> load(String query) {
        return Observable.create(e -> createSearchRequest(query)
                .enqueue(new SearchCallback(e)));
    }

    private Call<Search> createSearchRequest(String query) throws Exception{
        throw new SocketTimeoutException("Fake");
        //return searchService.tweets(query, null, null, null, "recent", MAX_ITEM_PER_REQUEST, null, null, null, true);
    }

    private static class SearchCallback extends Callback<Search> {

        private final ObservableEmitter<Search> searchEmitter;

        SearchCallback(ObservableEmitter<Search> searchEmitter) {
            this.searchEmitter = searchEmitter;
        }

        @Override
        public void success(Result<Search> result) {
            searchEmitter.onNext(result.data);
            searchEmitter.onComplete();
        }

        @Override
        public void failure(TwitterException e) {
            searchEmitter.onError(e);
        }
    }
}
