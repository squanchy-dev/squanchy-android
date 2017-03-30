package net.squanchy.signin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

import net.squanchy.R;
import net.squanchy.fonts.TypefaceStyleableActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class SignInActivity extends TypefaceStyleableActivity {

    private static final int RC_SIGN_IN = 9001;

    private SignInService service;
    private GoogleApiClient googleApiClient;

    private View progressView;
    private View signInContent;

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
    }

    private GoogleApiClient connectToGoogleApis() {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        return new GoogleApiClient.Builder(this)
                .enableAutoManage(this, e -> showSignInFailedError())
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
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                        // no op
                    }
                });
    }

    private void setupWindowParameters() {
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                showSignInFailedError();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        showProgress();

        service.signInWithGoogle(account)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(this::finish);
    }

    private void showProgress() {
        signInContent.setEnabled(false);
        signInContent.setAlpha(.54f);
        progressView.setVisibility(View.VISIBLE);
    }

    private void showSignInFailedError() {
        hideProgress();
        Snackbar.make(signInContent, R.string.sign_in_error_please_retry, Snackbar.LENGTH_SHORT).show();
    }

    private void hideProgress() {
        signInContent.setEnabled(true);
        signInContent.setAlpha(1f);
        progressView.setVisibility(View.GONE);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}
