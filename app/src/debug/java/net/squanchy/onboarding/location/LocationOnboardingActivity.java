package net.squanchy.onboarding.location;

import android.os.Bundle;
import android.support.annotation.Nullable;

import net.squanchy.R;
import net.squanchy.fonts.TypefaceStyleableActivity;
import net.squanchy.onboarding.Onboarding;
import net.squanchy.service.proximity.injection.ProximityService;

public class LocationOnboardingActivity extends TypefaceStyleableActivity {

    private Onboarding onboarding;
    private ProximityService service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocationOnboardingComponent component = LocationOnboardingInjector.obtain(this);
        onboarding = component.onboarding();
        service = component.proximityService();

        setContentView(R.layout.activity_location_onboarding);

        setResult(RESULT_CANCELED);
    }

    
}
