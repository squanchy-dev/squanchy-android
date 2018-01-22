package net.squanchy.tweets.domain.view

data class TwitterScreenViewModel(
        val hashtag: String,
        val tweets: List<TweetViewModel>
)
