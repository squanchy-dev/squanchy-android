package net.squanchy.venue

import io.reactivex.Observable
import net.squanchy.service.firebase.FirebaseAuthService
import net.squanchy.service.repository.VenueRepository
import net.squanchy.venue.domain.view.Venue

internal class VenueInfoService(private val venueRepository: VenueRepository, private val authService: FirebaseAuthService) {

    fun venue(): Observable<Venue> = authService.ifUserSignedInThenObservableFrom { venueRepository.venue() }
}
