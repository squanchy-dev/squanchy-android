package net.squanchy.service.proximity.injection;

import net.squanchy.proximity.ProximityEvent;
import net.squanchy.proximity.ProximityProvider;

import io.reactivex.Observable;

public class ProximityService {

    private final ProximityProvider proximityProvider;

    public ProximityService(ProximityProvider provider) {
        this.proximityProvider = provider;
    }

    public Observable<ProximityEvent> observeProximityEvents(){
        return proximityProvider.observeProximityEvents();
    }

    public void startRadar(){
        proximityProvider.startRadar();
    }

}
