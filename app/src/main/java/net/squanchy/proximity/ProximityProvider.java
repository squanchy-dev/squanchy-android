package net.squanchy.proximity;

import io.reactivex.Observable;

public interface ProximityProvider {

    void startRadar();

    void stopRadar();

    Observable<ProximityEvent> observeProximityEvents();
}
