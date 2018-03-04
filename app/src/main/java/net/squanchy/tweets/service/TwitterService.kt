package net.squanchy.tweets.service

import io.reactivex.Single
import net.squanchy.tweets.domain.view.TweetViewModel
import net.squanchy.tweets.view.TweetUrlSpanFactory

internal class TwitterService(
    private val repository: TwitterRepository,
    private val factory: TweetUrlSpanFactory
) {

    fun refresh(query: String): Single<List<TweetViewModel>> {
        return repository.load(query)
            .map { search -> search.tweets }
            .map { list -> list.filter { tweet -> tweet.retweetedStatus == null } }
            .map { tweets -> tweets.map { mapToViewModel(factory, it) } }
    }
}
