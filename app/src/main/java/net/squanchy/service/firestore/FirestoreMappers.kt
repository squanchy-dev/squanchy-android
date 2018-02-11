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

fun FirestoreEvent.toEvent(checksum: Checksum, timeZone: DateTimeZone) = Event(
    id,
    checksum.getChecksumOf(id),
    LocalDateTime(startTime),
    LocalDateTime(endTime),
    title,
    place.optional().map { it.toPlace() },
    track.optional().map { it.toTrack() },
    speakers.map { it.toSpeaker(checksum) },
    ExperienceLevel.tryParsingFrom(experienceLevel),
    Event.Type.fromRawType(type),
    false, // TODO fetch favourites
    description.optional(),
    timeZone
)

fun FirestorePlace.toPlace(): Place = Place.create(id, name, floor.optional())

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
