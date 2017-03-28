package net.squanchy.tweets.view;

import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;

import net.squanchy.tweets.service.TwitterRepository;
import net.squanchy.tweets.service.TwitterService;

import io.reactivex.Observable;

class TwitterItemAdapter {

    private final TwitterService twitterService;
    private boolean hasMoreItems = true;

    TwitterItemAdapter(TwitterRepository repo) {
        this.twitterService = new TwitterService(repo);
    }

    Tweet itemAt(int position) {
        return twitterService.itemAt(position);
    }

    /**
     * We add 1 to the number of tweets if the tweets are being loaded, 0 otherwise
     */
    int size() {
        return twitterService.size() + (hasMoreItems ? 1 : 0);
    }

    boolean isEmpty() {
        return twitterService.size() == 0;
    }

    Observable<Search> refresh() {
        return twitterService.refresh()
                .doOnNext(search -> hasMoreItems = !search.tweets.isEmpty());
    }

    Observable<Search> previous() {
        return twitterService.previous()
                .doOnNext(search -> hasMoreItems = !search.tweets.isEmpty());
    }

    @TweetViewTypeId
    int getItemViewType(int position) {
        return isLoadingItem(position) ? TweetViewTypeId.LOADING : TweetViewTypeId.TWEET;
    }

    private boolean isLoadingItem(int position) {
        return position == size() - 1 && hasMoreItems;
    }
}
