package net.squanchy.venue;

import net.squanchy.venue.domain.view.Venue;
import net.squanchy.service.repository.VenueRepository;

import io.reactivex.Observable;

class VenueInfoService {

    private final VenueRepository venueRepository;

    VenueInfoService(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    public Observable<Venue> venue() {
        return venueRepository.venue();
    }
}
