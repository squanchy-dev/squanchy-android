package net.squanchy.venue

import io.reactivex.Observable
import net.squanchy.service.firebase.FirebaseAuthService
import net.squanchy.service.firestore.FirestoreDbService
import net.squanchy.venue.domain.view.Venue
import org.joda.time.DateTimeZone

internal class VenueInfoService(private val dbService: FirestoreDbService, private val authService: FirebaseAuthService) {

    fun venue(): Observable<Venue> {
        return authService.ifUserSignedInThenObservableFrom {
            dbService.venueInfo()
                .map { firestoreVenue ->
                    Venue(
                            name = firestoreVenue.name,
                            address = firestoreVenue.address,
                            latitude = firestoreVenue.latLon.latitude,
                            longitude = firestoreVenue.latLon.longitude,
                            description = firestoreVenue.description,
                            mapUrl = firestoreVenue.mapUrl,
                            timeZone = DateTimeZone.forID(firestoreVenue.timezone)
                    )
                }
        }
    }
}
