package net.squanchy.tweets.view;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.Timeline;
import com.twitter.sdk.android.tweetui.TimelineResult;

import net.squanchy.tweets.service.TwitterService;

class TwitterRepository {

    private final TwitterService twitterService;
    private boolean hasMoreItems = true;

    TwitterRepository(Timeline<Tweet> timeline) {
        this.twitterService = new TwitterService(timeline);
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

    void refresh(Callback<TimelineResult<Tweet>> callback) {
        twitterService.refresh(callback);
    }

    void previous(Callback<TimelineResult<Tweet>> callback) {
        twitterService.previous(new CallbackDecorator(callback));
    }

    @TweetViewTypeId
    int getItemViewType(int position) {
        return isLoadingItem(position) ? TweetViewTypeId.LOADING : TweetViewTypeId.TWEET;
    }

    private boolean isLoadingItem(int position) {
        return position == size() - 1 && hasMoreItems;
    }

    private class CallbackDecorator extends Callback<TimelineResult<Tweet>> {

        private final Callback<TimelineResult<Tweet>> decorated;

        private CallbackDecorator(Callback<TimelineResult<Tweet>> decorated) {
            this.decorated = decorated;
        }

        @Override
        public void success(Result<TimelineResult<Tweet>> result) {
            if (result.data.items.isEmpty()) {
                hasMoreItems = false;
            }
            decorated.success(result);
        }

        @Override
        public void failure(TwitterException exception) {
            decorated.failure(exception);
        }
    }
}
