package net.squanchy.tweets.domain.view

import net.squanchy.tweets.domain.TweetLinkInfo

@Suppress("LongParameterList") // This is just a big model - TODO refactor this to split it up
data class TweetViewModel(
    val id: Long,
    val text: String,
    val spannedText: CharSequence,
    val user: User,
    val createdAt: String,
    val photoUrl: String?,
    val linkInfo: TweetLinkInfo
) {

    companion object {

        fun create(
            id: Long,
            text: String,
            spannedText: CharSequence,
            user: User,
            createdAt: String,
            photoUrl: String?,
            linkInfo: TweetLinkInfo
        ): TweetViewModel = TweetViewModel(
                id,
                text,
                spannedText,
                user,
                createdAt,
                photoUrl,
                linkInfo
        )
    }
}
