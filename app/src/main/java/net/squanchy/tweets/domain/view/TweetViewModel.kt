package net.squanchy.tweets.domain.view

import net.squanchy.support.lang.Optional
import net.squanchy.tweets.domain.TweetLinkInfo

data class TweetViewModel(
        val id: Long,
        val text: String,
        val spannedText: CharSequence,
        val user: User,
        val createdAt: String,
        val photoUrl: Optional<String>,
        val linkInfo: TweetLinkInfo
) {

    companion object {

        fun create(
                id: Long,
                text: String,
                spannedText: CharSequence,
                user: User,
                createdAt: String,
                photoUrl: Optional<String>,
                linkInfo: TweetLinkInfo
        ): TweetViewModel = TweetViewModel(id, text, spannedText, user, createdAt, photoUrl, linkInfo)
    }
}
