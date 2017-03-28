package net.squanchy.tweets;

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
    TwitterService twitterService(TwitterRepository repository) {
        return new TwitterService(repository);
    }
}
