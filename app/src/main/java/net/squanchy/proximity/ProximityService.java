package net.squanchy.proximity;

import io.reactivex.Observable;

public interface ProximityService {
    Observable<ProximityEvent> observeProximityEvents();
    ProximityProvider provider();
}
