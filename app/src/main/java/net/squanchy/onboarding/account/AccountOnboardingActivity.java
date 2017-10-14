package net.squanchy.onboarding.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.concurrent.TimeUnit;

import net.squanchy.R;
import net.squanchy.fonts.TypefaceStyleableActivity;
import net.squanchy.navigation.Navigator;
import net.squanchy.onboarding.Onboarding;
import net.squanchy.onboarding.OnboardingPage;
import net.squanchy.signin.SignInService;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class AccountOnboardingActivity extends TypefaceStyleableActivity {

    private static final int REQUEST_CODE_SIGNIN = 1235;

    private static final float DISABLED_UI_ALPHA = .54f;
    private static final float ENABLED_UI_ALPHA = 1f;

    private Onboarding onboarding;

    private View contentRoot;
    private Navigator navigator;
    private SignInService signInService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AccountOnboardingComponent component = AccountOnboardingInjector.obtain(this);
        onboarding = component.onboarding();
        navigator = component.navigator();
        signInService = component.signInService();

        setContentView(R.layout.activity_onboarding_account);
        contentRoot = findViewById(R.id.onboarding_content_root);

        findViewById(R.id.skip_button).setOnClickListener(view -> markPageAsSeenAndFinish());
        findViewById(R.id.location_opt_in_button).setOnClickListener(view -> signInToGoogle());

        setResult(RESULT_CANCELED);
    }

    @Override
    protected void onStart() {
        super.onStart();

        disableUi();
        signInService.isSignedInToGoogle()
                .subscribeOn(AndroidSchedulers.mainThread())
                .timeout(3, TimeUnit.SECONDS)
                .subscribe(
                        signedIn -> {
                            if (signedIn) {
                                markPageAsSeenAndFinish();
                            } else {
                                enableUi();
                            }
                        },
                        throwable -> enableUi()
                );
    }

    private void signInToGoogle() {
        disableUi();
        navigator.toSignInForResult(REQUEST_CODE_SIGNIN);
    }

    private void disableUi() {
        contentRoot.setEnabled(false);
        contentRoot.setAlpha(DISABLED_UI_ALPHA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != REQUEST_CODE_SIGNIN) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        if (resultCode == RESULT_OK) {
            markPageAsSeenAndFinish();
        } else {
            enableUi();
        }
    }

    void markPageAsSeenAndFinish() {
        onboarding.savePageSeen(OnboardingPage.ACCOUNT);
        setResult(RESULT_OK);
        finish();
    }

    private void enableUi() {
        contentRoot.setEnabled(true);
        contentRoot.setAlpha(ENABLED_UI_ALPHA);
    }
}
