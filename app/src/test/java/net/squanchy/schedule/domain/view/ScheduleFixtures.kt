package net.squanchy.schedule.domain.view

import net.squanchy.eventdetails.domain.view.ExperienceLevel
import net.squanchy.speaker.domain.view.Speaker
import net.squanchy.speaker.domain.view.aSpeaker
import net.squanchy.support.lang.Optional
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime

private val A_TIMEZONE = DateTimeZone.forID("Europe/Rome")

fun aSchedule(
    pages: List<SchedulePage> = listOf(aSchedulePage()),
    timezone: DateTimeZone = A_TIMEZONE
) = Schedule(
    pages = pages,
    timeZone = timezone
)

fun aSchedulePage(
    dayId: String = aDay().id,
    date: LocalDate = aDay().date,
    events: List<Event> = (0..3).map { anEvent() }
) = SchedulePage(
    dayId = dayId,
    date = date,
    events = events
)

fun aDay(
    id: String = "dayId",
    date: LocalDate = LocalDate(123456789, A_TIMEZONE)
) = Day(
    id = id,
    date = date
)

fun anEvent(
    id: String = "banana",
    numericId: Long = 1234,
    startTime: LocalDateTime = LocalDateTime(123456789),
    endTime: LocalDateTime = LocalDateTime(123459999),
    title: String = "Hello \uD83C\uDF4C", // Yes, that's a banana emoji. You never know
    place: Optional<Place> = Optional.of(aPlace()),
    experienceLevel: Optional<ExperienceLevel> = Optional.of(ExperienceLevel.BEGINNER),
    speakers: List<Speaker> = listOf(aSpeaker()),
    type: Event.Type = Event.Type.KEYNOTE,
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

fun aPlace(
    id: String = "banana-room",
    name: String = "The banana room™",
    floor: Optional<String> = Optional.of("Banana floor")
) = Place(
    id = id,
    name = name,
    floor = floor
)

fun aTrack(
    id: String = "a track id",
    numericId: Long = 0,
    name: String = "a track name",
    accentColor: Optional<String> = Optional.of("#ABCDEF"),
    textColor: Optional<String> = Optional.of("#FEDCBA"),
    iconUrl: Optional<String> = Optional.of("www.squanchy.net")
) = Track(
    id = id,
    numericId = numericId,
    name = name,
    accentColor = accentColor,
    textColor = textColor,
    iconUrl = iconUrl
)
