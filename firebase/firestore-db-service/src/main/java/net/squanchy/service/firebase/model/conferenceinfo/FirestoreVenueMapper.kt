package net.squanchy.service.firebase.model.conferenceinfo

import net.squanchy.venue.domain.view.Venue
import org.joda.time.DateTimeZone


fun FirestoreVenue.toVenue() = Venue(
    name = name,
    address = address,
    latitude = latLon.latitude,
    longitude = latLon.longitude,
    description = description,
    mapUrl = mapUrl,
    timeZone = DateTimeZone.forID(timezone)
)