package net.squanchy.venue.domain.view

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
) {

    companion object {

        fun create(
                name: String,
                address: String,
                latitude: Double,
                longitude: Double,
                description: String,
                mapUrl: String,
                timezoneId: String
        ) = Venue(
                name,
                address,
                latitude,
                longitude,
                description,
                mapUrl,
                DateTimeZone.forID(timezoneId)
        )
    }
}
