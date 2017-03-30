package net.squanchy.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import net.squanchy.fonts.TypefaceStyleableActivity;
import net.squanchy.navigation.deeplink.DeepLinkRouter;
import net.squanchy.onboarding.Onboarding;
import net.squanchy.onboarding.OnboardingPage;
import net.squanchy.signin.SignInService;
import net.squanchy.support.lang.Optional;

import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class RoutingActivity extends TypefaceStyleableActivity {

    private static final int ONBOARDING_REQUEST_CODE = 2453;

    private DeepLinkRouter deepLinkRouter;
    private Navigator navigator;
    private Onboarding onboarding;
    private Disposable subscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RoutingComponent component = RoutingInjector.obtain(this);
        deepLinkRouter = component.deepLinkRouter();
        navigator = component.navigator();
        onboarding = component.onboarding();

        SignInService signInService = component.signInService();
        subscription = signInService.signInAnonymouslyIfNecessary().subscribe();

        routeTo(getIntent());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ONBOARDING_REQUEST_CODE) {
            handleOnboardingResult(resultCode);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleOnboardingResult(int resultCode) {
        if (resultCode == RESULT_OK) {
            routeTo(getIntent());
        } else {
            finish();
        }
    }

    private void routeTo(Intent intent) {
        Optional<OnboardingPage> onboardingPageToShow = onboarding.nextPageToShow();
        if (onboardingPageToShow.isPresent()) {
            navigator.toOnboardingForResult(onboardingPageToShow.get(), ONBOARDING_REQUEST_CODE);
        } else {
            proceedTo(intent);
        }
    }

    private void proceedTo(Intent intent) {
        if (deepLinkRouter.hasDeepLink(intent)) {
            String intentUriString = intent.getDataString();
            Timber.i("Deeplink detected, navigating to \"%s\"", intentUriString);
            deepLinkRouter.navigateTo(intentUriString);
        } else {
            navigator.toSchedule();
        }

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.dispose();
    }
}
