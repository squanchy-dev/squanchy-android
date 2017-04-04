package net.squanchy.onboarding.location;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import net.squanchy.R;
import net.squanchy.fonts.TypefaceStyleableActivity;
import net.squanchy.onboarding.Onboarding;
import net.squanchy.onboarding.OnboardingPage;
import net.squanchy.proximity.preconditions.ProximityOptInPersister;
import net.squanchy.proximity.preconditions.ProximityPreconditions;
import net.squanchy.proximity.preconditions.TaskLauncherFactory;
import net.squanchy.service.proximity.injection.ProximityService;

import timber.log.Timber;

public class LocationOnboardingActivity extends TypefaceStyleableActivity {

    private Onboarding onboarding;
    private ProximityService service;
    private ProximityOptInPersister proximityOptInPersister;

    private ProximityPreconditions proximityPreconditions;

    private View contentRoot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, connectionResult -> onGoogleConnectionFailed())
                .addApi(LocationServices.API)
                .build();

        LocationOnboardingComponent component = LocationOnboardingInjector.obtain(
                this,
                TaskLauncherFactory.forActivity(this),
                googleApiClient,
                proximityPreconditionsCallback()
        );
        onboarding = component.onboarding();
        service = component.proximityService();
        proximityPreconditions = component.proximityPreconditions();
        proximityOptInPersister = component.proximityOptInPersister();

        googleApiClient.connect();

        setContentView(R.layout.activity_location_onboarding);
        contentRoot = findViewById(R.id.onboarding_content_root);

        findViewById(R.id.skip_button).setOnClickListener(view -> optOutFromProximity());
        findViewById(R.id.location_opt_in_button).setOnClickListener(view -> optInToProximity());

        setResult(RESULT_CANCELED);
    }

    private void optInToProximity() {
        disableUi();
        proximityOptInPersister.storeUserOptedIn();
        if (proximityPreconditions.needsActionToSatisfyPreconditions()) {
            proximityPreconditions.startSatisfyingPreconditions();
        } else {
            markPageAsSeenAndFinish();
        }
    }

    private void optOutFromProximity() {
        proximityOptInPersister.storeUserOptedOut();
        markPageAsSeenAndFinish();
    }

    private void disableUi() {
        contentRoot.setEnabled(false);
        contentRoot.setAlpha(.54f);
    }

    private void onGoogleConnectionFailed() {
        Timber.e("Google Client connection failed");
        Snackbar.make(contentRoot, R.string.proximity_error_google_client_connection, Snackbar.LENGTH_LONG).show();
    }

    private ProximityPreconditions.Callback proximityPreconditionsCallback() {
        return new ProximityPreconditions.Callback() {

            @Override
            public void notOptedIn() {
                Timber.e(new IllegalStateException("Proximity onboarding shown when the user is not opted in"));
                showFatalProximityError(R.string.proximity_error_not_opted_in);
            }

            @Override
            public void featureDisabled() {
                Timber.e(new IllegalStateException("Proximity onboarding shown when the feature is disabled (missing BT or kill-switched)"));
                showFatalProximityError(R.string.proximity_error_remote_config_kill_switch);
            }

            @Override
            public void permissionDenied() {
                showNonFatalProximityError(Snackbar.make(contentRoot, R.string.proximity_error_permission_denied, Snackbar.LENGTH_LONG));
            }

            @Override
            public void locationProviderDenied() {
                showNonFatalProximityError(Snackbar.make(contentRoot, R.string.proximity_error_location_denied, Snackbar.LENGTH_LONG));
            }

            @Override
            public void bluetoothDenied() {
                showNonFatalProximityError(Snackbar.make(contentRoot, R.string.proximity_error_bluetooth_denied, Snackbar.LENGTH_LONG));
            }

            @Override
            public void allChecksPassed() {
                startRadarAndFinish();
            }

            @Override
            public void exceptionWhileSatisfying(Throwable throwable) {
                Timber.e(throwable, "Exception occurred while checking");
                showNonFatalProximityError(Snackbar.make(contentRoot, R.string.proximity_error_bluetooth_denied, Snackbar.LENGTH_LONG));
            }

            @Override
            public void recheckAfterActivityResult() {
                optInToProximity();
            }
        };
    }

    private void showFatalProximityError(@StringRes int snackbarText) {
        Snackbar.make(contentRoot, snackbarText, Snackbar.LENGTH_LONG)
                .addCallback(closeAfterFatalErrorIsDisplayed())
                .show();
    }

    private Snackbar.BaseCallback<Snackbar> closeAfterFatalErrorIsDisplayed() {
        return new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                markPageAsSeenAndFinish();
            }
        };
    }

    private void showNonFatalProximityError(Snackbar snackbar) {
        enableUi();
        snackbar.show();
    }

    private void enableUi() {
        contentRoot.setEnabled(true);
        contentRoot.setAlpha(1f);
    }

    private void startRadarAndFinish() {
        service.startRadar();
        markPageAsSeenAndFinish();
    }

    void markPageAsSeenAndFinish() {
        onboarding.savePageSeen(OnboardingPage.LOCATION);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean handled = proximityPreconditions.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!handled) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean handled = proximityPreconditions.onActivityResult(requestCode, resultCode, data);
        if (!handled) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
