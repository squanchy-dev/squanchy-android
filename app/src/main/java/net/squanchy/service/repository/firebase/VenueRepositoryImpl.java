package net.squanchy.service.repository.firebase;

import net.squanchy.service.firebase.FirebaseDbService;
import net.squanchy.service.repository.VenueRepository;
import net.squanchy.venue.domain.view.Venue;

import io.reactivex.Observable;

public class VenueRepositoryImpl implements VenueRepository {

    private final FirebaseDbService dbService;

    public VenueRepositoryImpl(FirebaseDbService dbService) {
        this.dbService = dbService;
    }

    public Observable<Venue> venue() {
        return dbService.venueInfo()
                .map(firebaseVenue -> Venue.Companion.create(
                        firebaseVenue.getName(),
                        firebaseVenue.getAddress(),
                        firebaseVenue.getLat(),
                        firebaseVenue.getLon(),
                        firebaseVenue.getDescription(),
                        firebaseVenue.getMap_url(),
                        firebaseVenue.getTimezone()
                ));
    }
}
