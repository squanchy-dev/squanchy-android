package net.squanchy.proximity.near;

import android.content.Context;

import net.squanchy.analytics.ProximityTrackingType;
import net.squanchy.proximity.ProximityEvent;
import net.squanchy.proximity.ProximityProvider;

import org.json.JSONException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.schedulers.Schedulers;
import it.near.sdk.NearItManager;
import it.near.sdk.geopolis.beacons.ranging.ProximityListener;
import it.near.sdk.reactions.customjson.CustomJSON;
import it.near.sdk.recipes.RecipesManager;
import timber.log.Timber;

public class NearITProximityProvider implements ProximityProvider {

    private final NearItManager nearItManager;
    private static final String PROXIMITY_ACTION = "action";
    private static final String SUBJECT_ID = "subject";

    public NearITProximityProvider(NearItManager nearItManager) {
        this.nearItManager = nearItManager;
        // TODO set a notification icon if needed
    }

    @Override
    public void startRadar() {
        nearItManager.startRadar();
    }

    @Override
    public void stopRadar() {
        nearItManager.stopRadar();
    }

    @Override
    public Observable<ProximityEvent> observeProximityEvents() {
        return Observable.create((ObservableEmitter<ProximityEvent> e) -> {
            ProximityListener listener = (parcelable, recipe) -> {
                if (parcelable instanceof CustomJSON) {
                    CustomJSON customJSON = (CustomJSON) parcelable;
                    String action = (String) customJSON.getContent().get(PROXIMITY_ACTION);
                    String subject = (String) customJSON.getContent().get(SUBJECT_ID);
                    if (action != null && subject != null) {
                        e.onNext(ProximityEvent.create(recipe.getId(), action, subject));
                    }
                }
            };
            nearItManager.addProximityListener(listener);

            e.setCancellable(() -> nearItManager.removeProximityListener(listener));
        }).observeOn(Schedulers.io());
    }

    @Override
    public void trackProximityEvent(Context context, ProximityEvent proximityEvent, ProximityTrackingType trackingType) {
        try {
            RecipesManager.sendTracking(context, proximityEvent.id(), trackingType.rawTrackingType());
        } catch (JSONException e) {
            Timber.d("Proximity tracking could not be sent to NearIT");
        }
    }
}
