package net.squanchy.service.firebase

import arrow.core.Option
import net.squanchy.eventdetails.domain.view.ExperienceLevel
import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.domain.view.Place
import net.squanchy.schedule.domain.view.Track
import net.squanchy.service.firebase.model.conferenceinfo.FirestoreVenue
import net.squanchy.service.firebase.model.schedule.FirestoreEvent
import net.squanchy.service.firebase.model.schedule.FirestorePlace
import net.squanchy.service.firebase.model.schedule.FirestoreSpeaker
import net.squanchy.service.firebase.model.schedule.FirestoreTrack
import net.squanchy.speaker.domain.view.Speaker
import net.squanchy.support.checksum.Checksum
import net.squanchy.venue.domain.view.Venue
import org.joda.time.DateTimeZone
import org.joda.time.LocalDateTime

fun FirestorePlace.toPlace(): Place = Place(id = id, name = name, floor = Option.fromNullable(floor))

fun FirestoreTrack.toTrack(checksum: Checksum) = Track(
    id = id,
    numericId = checksum.getChecksumOf(id),
    name = name,
    accentColor = Option.fromNullable(accentColor),
    textColor = Option.fromNullable(textColor),
    iconUrl = Option.fromNullable(iconUrl)
)

fun FirestoreSpeaker.toSpeaker(checksum: Checksum) = Speaker(
    numericId = checksum.getChecksumOf(id),
    id = id,
    name = name,
    bio = bio,
    companyName = Option.fromNullable(companyName),
    companyUrl = Option.fromNullable(companyUrl),
    personalUrl = Option.fromNullable(personalUrl),
    photoUrl = Option.fromNullable(photoUrl),
    twitterUsername = Option.fromNullable(twitterUsername)
)

fun FirestoreVenue.toVenue() = Venue(
    name = name,
    address = address,
    latitude = latLon.latitude,
    longitude = latLon.longitude,
    description = description,
    mapUrl = mapUrl,
    timeZone = DateTimeZone.forID(timezone)
)

fun FirestoreEvent.toEvent(checksum: Checksum, timeZone: DateTimeZone, isFavorite: Boolean = false) = Event(
    id = id,
    numericId = checksum.getChecksumOf(id),
    startTime = LocalDateTime(startTime, timeZone),
    endTime = LocalDateTime(endTime, timeZone),
    title = title,
    place = Option.fromNullable(place?.toPlace()),
    experienceLevel = experienceLevel.toExperienceLevel(),
    speakers = speakers.map { it.toSpeaker(checksum) },
    type = type.toEventType(),
    favorited = isFavorite,
    description = Option.fromNullable(description),
    track = Option.fromNullable(track?.toTrack(checksum)),
    timeZone = timeZone
)

private fun String?.toExperienceLevel() = ExperienceLevel.tryParsingFrom(this)
private fun String.toEventType() = Event.Type.fromRawType(this)
