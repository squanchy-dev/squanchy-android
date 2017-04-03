package net.squanchy.analytics;

import android.content.Context;

import net.squanchy.proximity.ProximityEvent;
import net.squanchy.proximity.ProximityProvider;

public class ProximityAnalytics {

    private final ProximityProvider proximityProvider;
    private final Context context;

    public ProximityAnalytics(Context context, ProximityProvider proximityProvider) {
        this.context = context;
        this.proximityProvider = proximityProvider;
    }

    public void trackProximityEvent(ProximityEvent proximityEvent, ProximityTrackingType trackingType) {
        proximityProvider.trackProximityEvent(context, proximityEvent, trackingType);
    }
}
