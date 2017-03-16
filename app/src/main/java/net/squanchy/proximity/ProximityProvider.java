package net.squanchy.proximity;

import io.reactivex.Observable;

public interface ProximityProvider {
    void startRadar();
    Observable<ProximityEvent> observeProximityEvents();
}
