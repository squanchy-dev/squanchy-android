package net.squanchy.proximity.preconditions;

import android.content.IntentSender;
import android.location.LocationManager;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import net.squanchy.support.lang.Optional;

import io.reactivex.Single;

public class LocationProviderPrecondition implements Precondition {

    private static final int REQUEST_ENABLE_LOCATION_PROVIDER = 8999;

    private static final int REQUEST_INTERVAL_MS = 10000;
    private static final int REQUEST_FASTEST_INTERVAL_MS = 5000;

    private final TaskLauncher taskLauncher;
    private final GoogleApiClient googleApiClient;
    private final LocationManager locationManager;

    LocationProviderPrecondition(TaskLauncher taskLauncher, GoogleApiClient googleApiClient, LocationManager locationManager) {
        this.taskLauncher = taskLauncher;
        this.googleApiClient = googleApiClient;
        this.locationManager = locationManager;
    }

    @Override
    public boolean satisfied() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public Single<SatisfyResult> satisfy() {
        return Single.create(emitter -> {
            LocationSettingsRequest settingsRequest = createLocationSettingsRequest();
            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, settingsRequest);

            result.setResultCallback(locationSettingsResult -> {
                Status status = locationSettingsResult.getStatus();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        emitter.onSuccess(SatisfyResult.SUCCESS);
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            taskLauncher.startIntentSenderForResult(status.getResolution().getIntentSender(), REQUEST_ENABLE_LOCATION_PROVIDER);
                            emitter.onSuccess(SatisfyResult.WAIT_FOR_EXTERNAL_RESULT);
                        } catch (IntentSender.SendIntentException e) {
                            emitter.onSuccess(SatisfyResult.ABORT);
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        emitter.onSuccess(SatisfyResult.ABORT);
                        break;
                }
            });
        });
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

    @Override
    public Optional<Integer> requestCode() {
        return Optional.of(REQUEST_ENABLE_LOCATION_PROVIDER);
    }
}
