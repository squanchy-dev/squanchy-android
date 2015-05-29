package com.ls.drupalconapp.ui.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.ls.drupalconapp.R;
import com.ls.drupalconapp.ui.dialog.LoadingDialog;
import com.ls.utils.UIUtils;

// not used for now
public class LoginActivity extends StateActivity {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_login);

        initView();
    }

    private void initView() {
        final View btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator animRight = ObjectAnimator.ofFloat(btnLogin, "x", UIUtils.dipToPixels(LoginActivity.this, getRightAnimationPixels()));
                animRight.setDuration(350);
                ObjectAnimator animLeft = ObjectAnimator.ofFloat(btnLogin, "x", UIUtils.dipToPixels(LoginActivity.this, -400));
                animLeft.setDuration(500);

                AnimatorSet animSet = new AnimatorSet();
                animSet.play(animRight).before(animLeft);
                animSet.start();

                btnLogin.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        emulateLogin();
                    }
                }, 900);
            }
        });
    }

    private void emulateLogin() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                showLoadingDialog();
            }

            @Override
            protected Void doInBackground(Void... params) {

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                hideLoadingDialog();
                startMainActivity();
                finish();
            }
        }.execute();

    }

    private void showLoadingDialog() {
        LoadingDialog dialog = LoadingDialog.newInstance(getString(R.string.log_in_));

        getFragmentManager().beginTransaction()
                .add(dialog, LoadingDialog.TAG)
                .commitAllowingStateLoss();
    }

    private void hideLoadingDialog() {
        LoadingDialog dialog = (LoadingDialog)getFragmentManager().findFragmentByTag(LoadingDialog.TAG);

        if(dialog != null) {
            dialog.dismissAllowingStateLoss();;
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }

    private int getRightAnimationPixels() {
        return getResources().getDisplayMetrics().density == 2 ? 150 : 100;
    }
}
