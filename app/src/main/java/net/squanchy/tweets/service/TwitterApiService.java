package net.squanchy.tweets.service;

import com.twitter.sdk.android.core.models.Search;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TwitterApiService {

    @GET("/1.1/search/tweets.json?tweet_mode=extended&include_entities=true")
    Observable<Search> tweets(@Query("q") String query,
                              @Query("result_type") String type,
                              @Query("count") Integer count,
                              @Query("until") String until,
                              @Query("since_id") Long since,
                              @Query("max_id") Long max);
}
