package net.squanchy.speaker.domain.view

import net.squanchy.support.lang.Optional

@Suppress("LongParameterList") // This is just a big model - TODO refactor this to split it up
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
)
