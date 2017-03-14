package net.squanchy.proximity.near;

import android.os.Parcelable;

import net.squanchy.proximity.ProximityEvent;
import net.squanchy.proximity.ProximityProvider;
import net.squanchy.proximity.ProximityService;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.schedulers.Schedulers;
import it.near.sdk.Geopolis.Beacons.Ranging.ProximityListener;
import it.near.sdk.Reactions.CustomJSON.CustomJSON;
import it.near.sdk.Reactions.CustomJSON.CustomJSONReaction;
import it.near.sdk.Recipes.Models.Recipe;

public class NearITProximityService implements ProximityService {

    private static final String PROXIMITY_ACTION = "action";
    private static final String SUBJECT_ID = "subject";
    private final NearITProximityProvider nearITProximityProvider;

    public NearITProximityService(ProximityProvider proximityProvider) {
        this.nearITProximityProvider = (NearITProximityProvider) proximityProvider;
    }

    @Override
    public Observable<ProximityEvent> observeProximityEvents() {
        return Observable.create((ObservableEmitter<ProximityEvent> e) -> {
            ProximityListener listener = (parcelable, recipe) -> {
                if (parcelable instanceof CustomJSON) {
                    CustomJSON customJSON = (CustomJSON) parcelable;
                    String action = (String) customJSON.getContent().get(PROXIMITY_ACTION);
                    String subject = (String) customJSON.getContent().get(SUBJECT_ID);
                    if (action != null && subject != null)
                        e.onNext(new ProximityEvent(action, subject));
                }
            };
            nearITProximityProvider.registerForProximityEvents(listener);

            e.setCancellable(() -> nearITProximityProvider.deregisterForProximityEvents(listener));
        }).observeOn(Schedulers.io());
    }

    @Override
    public ProximityProvider provider() {
        return nearITProximityProvider;
    }
}
