package net.squanchy.proximity.preconditions;

import android.app.Activity;
import android.content.IntentSender;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.auto.value.AutoValue;

import net.squanchy.support.lang.Optional;

import io.reactivex.Completable;

public class LocationProviderPrecondition implements Precondition {

    private static final int REQUEST_ENABLE_LOCATION_PROVIDER = 8999;
    
    private static final int REQUEST_INTERVAL_MS = 10000;
    private static final int REQUEST_FASTEST_INTERVAL_MS = 5000;

    private final Activity activity;
    private final GoogleApiClient googleApiClient;

    private boolean satisfied = ALWAYS_ATTEMPT_SATISFYING;

    LocationProviderPrecondition(Activity activity, GoogleApiClient googleApiClient) {
        this.activity = activity;
        this.googleApiClient = googleApiClient;
    }

    @Override
    public boolean available() {
        return true;
    }

    @Override
    public boolean satisfied() {
        return satisfied;
    }

    @Override
    public Completable satisfy() {
        return Completable.create(emitter -> {
            LocationSettingsRequest settingsRequest = createLocationSettingsRequest();
            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, settingsRequest);

            result.setResultCallback(locationSettingsResult -> {
                Status status = locationSettingsResult.getStatus();
                LocationSettingsStates locationSettingsStates = locationSettingsResult.getLocationSettingsStates();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        satisfied = true;
                        emitter.onComplete();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        satisfied = false;
                        try {
                            status.startResolutionForResult(activity, REQUEST_ENABLE_LOCATION_PROVIDER);
                            emitter.onComplete();
                        } catch (IntentSender.SendIntentException e) {
                            FailureInfo failure = FailureInfo.from(locationSettingsStates);
                            emitter.onError(new ProviderPreconditionException(failure, e));
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        satisfied = false;
                        FailureInfo failure = FailureInfo.from(locationSettingsStates);
                        emitter.onError(new ProviderPreconditionException(failure));
                        break;
                }
            });
        });
    }

    @Override
    public Optional<Integer> requestCode() {
        return Optional.of(REQUEST_ENABLE_LOCATION_PROVIDER);
    }

    private LocationSettingsRequest createLocationSettingsRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(REQUEST_INTERVAL_MS);
        locationRequest.setFastestInterval(REQUEST_FASTEST_INTERVAL_MS);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        return new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .build();
    }

    public static class ProviderPreconditionException extends RuntimeException {

        private final FailureInfo failureInfo;

        ProviderPreconditionException(FailureInfo failureInfo) {
            super("Error while trying to check/enable the location provider");
            this.failureInfo = failureInfo;
        }

        ProviderPreconditionException(FailureInfo failureInfo, Throwable cause) {
            super("Error while trying to check/enable the location provider", cause);
            this.failureInfo = failureInfo;
        }

        public FailureInfo failureInfo() {
            return failureInfo;
        }
    }

    @AutoValue
    public abstract static class FailureInfo {

        public static FailureInfo from(LocationSettingsStates locationSettingsStates) {
            return new AutoValue_LocationProviderPrecondition_FailureInfo(
                    locationSettingsStates.isBlePresent(),
                    locationSettingsStates.isBleUsable(),
                    locationSettingsStates.isGpsPresent(),
                    locationSettingsStates.isGpsUsable(),
                    locationSettingsStates.isLocationPresent(),
                    locationSettingsStates.isLocationUsable(),
                    locationSettingsStates.isNetworkLocationPresent(),
                    locationSettingsStates.isNetworkLocationUsable()
            );
        }

        public abstract boolean blePresent();

        public abstract boolean bleUsable();

        public abstract boolean gpsPresent();

        public abstract boolean gpsUsable();

        public abstract boolean locationPresent();

        public abstract boolean locationUsable();

        public abstract boolean networkLocationPresent();

        public abstract boolean networkLocationUsable();
    }
}
