package net.squanchy.tweets.service;

import android.support.annotation.Nullable;

import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.services.SearchService;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TwitterRepository {

    private static final int MAX_ITEM_PER_REQUEST = 100;

    private final SearchService searchService = TwitterCore.getInstance().getApiClient().getSearchService();

    Observable<Search> load(String query) {
        return Observable.create(e -> createSearchRequest(query)
                .enqueue(new SearchCallback(e)));
    }

    private Call<Search> createSearchRequest(String query) throws Exception {
        return searchService.tweets(query, null, null, null, "recent", MAX_ITEM_PER_REQUEST, null, null, null, true);
    }

    private static class SearchCallback implements Callback<Search> {

        private final ObservableEmitter<Search> searchEmitter;

        SearchCallback(ObservableEmitter<Search> searchEmitter) {
            this.searchEmitter = searchEmitter;
        }

        @Override
        public void onResponse(Call<Search> call, Response<Search> response) {
            if (response.isSuccessful()) {
                searchEmitter.onNext(response.body());
                searchEmitter.onComplete();
            } else {
                onFailure(null, new RuntimeException("Unable to load tweets"));
            }
        }

        @Override
        public void onFailure(@Nullable Call<Search> call, Throwable throwable) {
            searchEmitter.onError(throwable);
        }
    }
}
