package net.squanchy.schedule.domain.view

import net.squanchy.eventdetails.domain.view.ExperienceLevel
import net.squanchy.speaker.domain.view.Speaker
import net.squanchy.support.lang.Optional
import org.joda.time.DateTimeZone
import org.joda.time.LocalDateTime

fun anEvent(
    id: String = "banana",
    numericId: Long = 1234,
    startTime: LocalDateTime = LocalDateTime(123456),
    endTime: LocalDateTime = LocalDateTime(123666),
    title: String = "Hello \uD83C\uDF4C", // Yes, that's a banana emoji. You never know
    place: Optional<Place> = Optional.of(aPlace()),
    experienceLevel: Optional<ExperienceLevel> = Optional.absent<ExperienceLevel>(),
    speakers: List<Speaker> = emptyList(),
    type: Event.Type = Event.Type.OTHER,
    description: Optional<String> = Optional.of("Now this is the story all about how\nMy life got flipped, turned upside down"),
    track: Optional<Track> = Optional.of(aTrack()),
    timeZone: DateTimeZone = DateTimeZone.forID("Europe/Rome"),
    favorited: Boolean = false
) = Event(
    id = id,
    numericId = numericId,
    startTime = startTime,
    endTime = endTime,
    title = title,
    place = place,
    experienceLevel = experienceLevel,
    speakers = speakers,
    type = type,
    description = description,
    track = track,
    timeZone = timeZone,
    favorited = favorited
)
