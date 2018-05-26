package net.squanchy.venue

import io.reactivex.Observable
import net.squanchy.venue.domain.view.Venue

interface VenueInfoService {
    fun venue(): Observable<Venue>
}
