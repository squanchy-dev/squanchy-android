package net.squanchy.service.firestore

import net.squanchy.eventdetails.domain.view.ExperienceLevel
import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.domain.view.Place
import net.squanchy.schedule.domain.view.Track
import net.squanchy.service.firestore.model.conferenceinfo.FirestoreVenue
import net.squanchy.service.firestore.model.schedule.FirestoreEvent
import net.squanchy.service.firestore.model.schedule.FirestorePlace
import net.squanchy.service.firestore.model.schedule.FirestoreSpeaker
import net.squanchy.service.firestore.model.schedule.FirestoreTrack
import net.squanchy.speaker.domain.view.Speaker
import net.squanchy.support.lang.Checksum
import net.squanchy.support.lang.optional
import net.squanchy.venue.domain.view.Venue
import org.joda.time.DateTimeZone
import org.joda.time.LocalDateTime

fun FirestorePlace.toPlace(): Place = Place(id = id, name = name, floor = floor.optional())

fun FirestoreTrack.toTrack() = Track(
    id = id,
    name = name,
    accentColor = accentColor.optional(),
    textColor = textColor.optional(),
    iconUrl = iconUrl.optional()
)

fun FirestoreSpeaker.toSpeaker(checksum: Checksum) = Speaker(
    numericId = checksum.getChecksumOf(id),
    id = id,
    name = name,
    bio = bio,
    companyName = companyName.optional(),
    companyUrl = companyUrl.optional(),
    personalUrl = personalUrl.optional(),
    photoUrl = photoUrl.optional(),
    twitterUsername = twitterUsername.optional()
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

fun FirestoreEvent.toEvent(checksum: Checksum, dateTime: DateTimeZone, isFavorite: Boolean = false) = Event(
    id = id,
    numericId = checksum.getChecksumOf(id),
    startTime = LocalDateTime(startTime),
    endTime = LocalDateTime(endTime),
    title = title,
    place = place?.toPlace().optional(),
    experienceLevel = experienceLevel.toExperienceLevel() ,
    speakers = speakers.map { it.toSpeaker(checksum) },
    type = type.toEventType(),
    favorited = isFavorite,
    description = description.optional(),
    track = track?.toTrack().optional(),
    timeZone = dateTime
)

private fun String?.toExperienceLevel() = ExperienceLevel.tryParsingFrom(this)
private fun String.toEventType() = Event.Type.fromRawType(this)
