package net.squanchy.search.domain.view

import net.squanchy.schedule.domain.view.Event
import net.squanchy.speaker.domain.view.Speaker

sealed class SearchListElement {
    object EventHeader : SearchListElement()
    data class EventElement(val event: Event) : SearchListElement()
    object SpeakerHeader : SearchListElement()
    data class SpeakerElement(val speaker: Speaker) : SearchListElement()
    object AlgoliaLogo : SearchListElement()
}
