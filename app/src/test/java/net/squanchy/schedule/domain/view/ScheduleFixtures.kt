package net.squanchy.schedule.domain.view

import arrow.core.Option
import net.squanchy.A_DATE
import net.squanchy.A_TIMEZONE
import net.squanchy.eventdetails.domain.view.ExperienceLevel
import net.squanchy.speaker.domain.view.Speaker
import net.squanchy.speaker.domain.view.aSpeaker
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime

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
    date: LocalDate = A_DATE.toDateTime(A_TIMEZONE).toLocalDate()
) = Day(
    id = id,
    date = date
)

fun anEvent(
    id: String = "banana",
    numericId: Long = 1234,
    startTime: LocalDateTime = A_DATE.toDateTime(A_TIMEZONE).toLocalDateTime(),
    endTime: LocalDateTime = A_DATE.plusMinutes(1).toDateTime(A_TIMEZONE).toLocalDateTime(),
    title: String = "Hello \uD83C\uDF4C", // Yes, that's a banana emoji. You never know
    place: Option<Place> = Option(aPlace()),
    experienceLevel: Option<ExperienceLevel> = Option(ExperienceLevel.BEGINNER),
    speakers: List<Speaker> = listOf(aSpeaker()),
    type: Event.Type = Event.Type.KEYNOTE,
    description: Option<String> = Option("Now this is the story all about how\nMy life got flipped, turned upside down"),
    track: Option<Track> = Option(aTrack()),
    timeZone: DateTimeZone = A_TIMEZONE,
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
    favorite = favorited
)

fun aPlace(
    id: String = "banana-room",
    name: String = "The banana room™",
    floor: Option<String> = Option("Banana floor"),
    position: Int = 0
) = Place(
    id = id,
    name = name,
    floor = floor,
    position = position
)

fun aTrack(
    id: String = "a track id",
    numericId: Long = 0,
    name: String = "a track name",
    accentColor: Option<String> = Option("#ABCDEF"),
    textColor: Option<String> = Option("#FEDCBA"),
    iconUrl: Option<String> = Option("www.squanchy.net")
) = Track(
    id = id,
    numericId = numericId,
    name = name,
    accentColor = accentColor,
    textColor = textColor,
    iconUrl = iconUrl
)
