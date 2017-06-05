package net.squanchy.proximity;

import android.content.Context;

import net.squanchy.analytics.ProximityTrackingType;

import io.reactivex.Observable;

public interface ProximityProvider {

    void startRadar();

    void stopRadar();

    Observable<ProximityEvent> observeProximityEvents();

    void trackProximityEvent(Context context, ProximityEvent proximityEvent, ProximityTrackingType trackingType);
}
