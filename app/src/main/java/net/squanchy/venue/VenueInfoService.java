package net.squanchy.venue;

import net.squanchy.service.firebase.FirebaseAuthService;
import net.squanchy.service.repository.VenueRepository;
import net.squanchy.venue.domain.view.Venue;

import io.reactivex.Observable;

class VenueInfoService {

    private final VenueRepository venueRepository;
    private final FirebaseAuthService authService;

    VenueInfoService(VenueRepository venueRepository, FirebaseAuthService authService) {
        this.venueRepository = venueRepository;
        this.authService = authService;
    }

    public Observable<Venue> venue() {
        return authService.ifUserSignedInThenObservableFrom(userId -> venueRepository.venue());
    }
}
