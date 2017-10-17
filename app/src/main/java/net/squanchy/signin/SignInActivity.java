package net.squanchy.signin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

import net.squanchy.R;
import net.squanchy.support.config.DialogLayoutParameters;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;

import static net.squanchy.google.GoogleClientId.SIGN_IN_ACTIVITY;

public class SignInActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;
    private static final float ALPHA_DISABLED = .54f;
    private static final float ALPHA_ENABLED = 1f;

    private SignInService service;
    private GoogleApiClient googleApiClient;

    private View progressView;
    private View signInContent;
    private Disposable subscription = Disposables.disposed();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        googleApiClient = connectToGoogleApis();

        SignInComponent component = SignInInjector.obtain(this);
        service = component.service();

        setContentView(R.layout.activity_signin);

        progressView = findViewById(R.id.progressbar);
        signInContent = findViewById(R.id.sign_in_content);

        findViewById(R.id.sign_in_button).setOnClickListener(view -> signIn());
        findViewById(R.id.touch_outside).setOnClickListener(v -> finish());

        View bottomSheet = findViewById(R.id.bottom_sheet);
        setBottomSheetCallbackOn(bottomSheet);

        setupWindowParameters();

        setResult(RESULT_CANCELED);
    }

    private GoogleApiClient connectToGoogleApis() {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        return new GoogleApiClient.Builder(this)
                .enableAutoManage(this, SIGN_IN_ACTIVITY.clientId(), e -> showSignInFailedError())
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();
    }

    private void setBottomSheetCallbackOn(View bottomSheet) {
        BottomSheetBehavior.from(bottomSheet)
                .setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        switch (newState) {
                            case BottomSheetBehavior.STATE_HIDDEN:
                                finish();
                                break;
                            default:
                                // Do nothing
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                        // no op
                    }
                });
    }

    private void setupWindowParameters() {
        Window window = getWindow();
        DialogLayoutParameters.wrapHeight(this)
                .applyTo(window);
        window.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result != null && result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                showSignInFailedError();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        showProgress();

        subscription = service.signInWithGoogle(account)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(onSignInSuccessful());
    }

    private Action onSignInSuccessful() {
        return () -> {
            setResult(RESULT_OK);
            finish();
        };
    }

    private void showProgress() {
        signInContent.setEnabled(false);
        signInContent.setAlpha(ALPHA_DISABLED);
        progressView.setVisibility(View.VISIBLE);
    }

    private void showSignInFailedError() {
        hideProgress();
        Snackbar.make(signInContent, R.string.sign_in_error_please_retry, Snackbar.LENGTH_SHORT).show();
    }

    private void hideProgress() {
        signInContent.setEnabled(true);
        signInContent.setAlpha(ALPHA_ENABLED);
        progressView.setVisibility(View.GONE);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onStop() {
        super.onStop();
        subscription.dispose();
    }
}
