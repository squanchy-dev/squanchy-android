package net.squanchy.proximity.near;

import android.content.Context;
import android.util.Log;

import net.squanchy.R;
import net.squanchy.proximity.ProximityProvider;

import it.near.sdk.Geopolis.Beacons.Ranging.ProximityListener;
import it.near.sdk.NearItManager;

public class NearITProximityProvider implements ProximityProvider{

    NearItManager nearItManager;

    public NearITProximityProvider(Context context) {
        Log.d("NearITProximityProvider", "new instance created");
        nearItManager = new NearItManager(context.getApplicationContext(), context.getString(R.string.nearit_api_key));
        // TODO set a notification icon if needed
    }

    public void registerForProximityEvents(ProximityListener listener) {
        nearItManager.addProximityListener(listener);
    }

    public void deregisterForProximityEvents(ProximityListener listener) {
        nearItManager.removeProximityListener(listener);
    }

    @Override
    public void startRadar() {
        nearItManager.startRadar();
    }
}
