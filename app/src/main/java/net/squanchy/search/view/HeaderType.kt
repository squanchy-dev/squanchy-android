package net.squanchy.search.view

import androidx.annotation.StringRes

import net.squanchy.R

enum class HeaderType(
    @param:StringRes
    @get:StringRes
    val headerTextResourceId: Int
) {
    SPEAKERS(R.string.speaker_list_title),
    EVENTS(R.string.talks_list_title),
    TRACKS(R.string.speaker_list_title)
}
