package net.squanchy.tweets;

import android.app.Activity;

import net.squanchy.injection.ApplicationContextModule;
import net.squanchy.tweets.service.TweetModelConverter;
import net.squanchy.tweets.service.TweetSpannedTextBuilder;
import net.squanchy.tweets.service.TwitterRepository;
import net.squanchy.tweets.service.TwitterService;
import net.squanchy.tweets.view.TweetUrlSpanFactory;

import dagger.Module;
import dagger.Provides;

@Module(includes = ApplicationContextModule.class)
class TwitterModule {

    @Provides
    TwitterRepository twitterRepository() {
        return new TwitterRepository();
    }

    @Provides
    TweetSpannedTextBuilder tweetSpannedTextBuilder(Activity activity) {
        TweetUrlSpanFactory spanFactory = new TweetUrlSpanFactory(activity);
        return new TweetSpannedTextBuilder(spanFactory);
    }

    @Provides
    TweetModelConverter tweetModelConverter(TweetSpannedTextBuilder tweetSpannedTextBuilder) {
        return new TweetModelConverter(tweetSpannedTextBuilder);
    }

    @Provides
    TwitterService twitterService(TwitterRepository repository, TweetModelConverter modelConverter) {
        return new TwitterService(repository, modelConverter);
    }
}
