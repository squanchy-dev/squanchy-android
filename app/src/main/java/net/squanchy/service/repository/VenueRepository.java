package net.squanchy.service.repository;

import net.squanchy.venue.domain.view.Venue;

import io.reactivex.Observable;

public interface VenueRepository {

    Observable<Venue> venue();
}
