package net.squanchy.venue.domain.view

import net.squanchy.service.firebase.model.conferenceinfo.FirestoreVenue
import org.joda.time.DateTimeZone

@Suppress("LongParameterList") // This is just a big model - TODO refactor this to split it up
data class Venue(
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val description: String,
    val mapUrl: String,
    val timeZone: DateTimeZone
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
