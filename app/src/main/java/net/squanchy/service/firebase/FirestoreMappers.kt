package net.squanchy.service.firebase

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
import net.squanchy.support.lang.option
import net.squanchy.venue.domain.view.Venue
import org.joda.time.DateTimeZone
import org.joda.time.LocalDateTime

fun FirestorePlace.toPlace(): Place = Place(id = id, name = name, floor = floor.option(), position = position)

fun FirestoreTrack.toTrack(checksum: Checksum) = Track(
    id = id,
    numericId = checksum.getChecksumOf("track_$id"),
    name = name,
    accentColor = accentColor,
    textColor = textColor,
    iconUrl = iconUrl
)

fun FirestoreSpeaker.toSpeaker(checksum: Checksum) = Speaker(
    numericId = checksum.getChecksumOf("speaker_$id"),
    id = id,
    name = name,
    bio = bio,
    companyName = companyName.option(),
    companyUrl = companyUrl.option(),
    personalUrl = personalUrl.option(),
    photoUrl = photoUrl.option(),
    twitterUsername = twitterUsername.option()
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
    numericId = checksum.getChecksumOf("event_$id"),
    startTime = LocalDateTime(startTime.toDate(), timeZone),
    endTime = LocalDateTime(endTime.toDate(), timeZone),
    title = title,
    place = place?.toPlace().option(),
    experienceLevel = experienceLevel.toExperienceLevel(),
    speakers = speakers.map { it.toSpeaker(checksum) },
    type = type.toEventType(),
    favorite = isFavorite,
    description = description.option(),
    track = track?.toTrack(checksum).option(),
    timeZone = timeZone
)

private fun String?.toExperienceLevel() = ExperienceLevel.tryParsingFrom(this)
private fun String.toEventType() = Event.Type.fromRawType(this)
