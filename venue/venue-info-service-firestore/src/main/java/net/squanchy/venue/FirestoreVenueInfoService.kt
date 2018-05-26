package net.squanchy.venue

import io.reactivex.Observable
import net.squanchy.service.firebase.FirestoreDbService
import net.squanchy.service.firebase.model.conferenceinfo.FirestoreVenue
import net.squanchy.service.firebase.model.conferenceinfo.toVenue
import net.squanchy.service.repository.AuthService
import net.squanchy.venue.domain.view.Venue

class FirestoreVenueInfoService(private val dbService: FirestoreDbService, private val authService: AuthService) : VenueInfoService {

    override fun venue(): Observable<Venue> {
        return authService.ifUserSignedInThenObservableFrom {
            dbService.venueInfo()
                .map(FirestoreVenue::toVenue)
        }
    }
}
