package net.squanchy.tweets.service;

import android.support.annotation.Nullable;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.services.SearchService;

import net.squanchy.tweets.model.TimelineState;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import retrofit2.Call;

public class TwitterRepository {

    private static final int MAX_ITEM_PER_REQUEST = 30;

    private final SearchService searchService;
    private TimelineState stateHolder;
    private final String query;
    private boolean refreshing = false;

    public TwitterRepository(String query) {
        this.searchService = TwitterCore.getInstance().getApiClient().getSearchService();
        this.query = query;
    }

    Observable<Search> refresh() {
        if (refreshing) {
            return Observable.empty();
        }
        refreshing = true;
        return wrapRequestWithObservable(null)
                .doOnNext(search -> stateHolder = TimelineState.init(search.tweets))
                .doOnTerminate(() -> refreshing = false);
    }

    Observable<Search> previous() {
        if (refreshing) {
            return Observable.empty();
        }
        refreshing = true;
        return wrapRequestWithObservable(decrement(stateHolder.positionForPrevious()))
                .doOnNext(search -> stateHolder.previous(search.tweets))
                .doOnTerminate(() -> refreshing = false);
    }

    private Observable<Search> wrapRequestWithObservable(@Nullable Long maxId) {
        return Observable.create(e -> createSearchRequest(maxId).enqueue(new SearchCallback(e)));
    }

    private Call<Search> createSearchRequest(@Nullable Long maxId) {
        return searchService.tweets(query, null, null, null, "recent", MAX_ITEM_PER_REQUEST, null, null, maxId, true);
    }

    private static Long decrement(Long maxId) {
        return maxId - 1L;
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
