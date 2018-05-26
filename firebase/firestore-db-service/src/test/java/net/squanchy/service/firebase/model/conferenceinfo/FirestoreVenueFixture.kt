package net.squanchy.service.firebase.model.conferenceinfo

import com.google.firebase.firestore.GeoPoint


fun aFirestoreVenue(
    name: String = "Venue Of The Conference",
    address: String = "Conference rd",
    latLon: GeoPoint = GeoPoint(0.0, 0.0),
    description: String = "This is the conference you're looking for",
    mapUrl: String = "www.maps.google.com/conference",
    timezone: String = "Europe/Rome"
) = FirestoreVenue().apply {
    this.name = name
    this.address = address
    this.latLon = latLon
    this.description = description
    this.mapUrl = mapUrl
    this.timezone = timezone
}