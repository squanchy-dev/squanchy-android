package net.squanchy.speaker.domain.view

import net.squanchy.support.lang.Optional

data class Speaker(
        val numericId: Long,
        val id: String,
        val name: String,
        val bio: String,
        val companyName: Optional<String>,
        val companyUrl: Optional<String>,
        val personalUrl: Optional<String>,
        val photoUrl: Optional<String>,
        val twitterUsername: Optional<String>
) {

    companion object {

        fun create(
                speakerId: String,
                numericSpeakerId: Long,
                name: String,
                bio: String,
                companyName: Optional<String>,
                companyUrl: Optional<String>,
                personalUrl: Optional<String>,
                photoUrl: Optional<String>,
                twitterUsername: Optional<String>
        ) = Speaker(
                numericSpeakerId,
                speakerId,
                name,
                bio,
                companyName,
                companyUrl,
                personalUrl,
                photoUrl,
                twitterUsername
        )
    }
}
