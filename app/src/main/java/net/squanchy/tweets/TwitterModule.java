package net.squanchy.tweets;

import net.squanchy.tweets.service.TweetModelConverter;
import net.squanchy.tweets.service.TwitterRepository;
import net.squanchy.tweets.service.TwitterService;

import dagger.Module;
import dagger.Provides;

@Module
class TwitterModule {

    @Provides
    TwitterRepository twitterRepository() {
        return new TwitterRepository();
    }

    @Provides
    TweetModelConverter tweetModelConverter() {
        return new TweetModelConverter();
    }

    @Provides
    TwitterService twitterService(TwitterRepository repository, TweetModelConverter modelConverter) {
        return new TwitterService(repository, modelConverter);
    }
}
