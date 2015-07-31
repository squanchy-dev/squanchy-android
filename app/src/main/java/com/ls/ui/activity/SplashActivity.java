package com.ls.ui.activity;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.WindowManager;

import com.ls.drupalconapp.R;
import com.ls.drupalconapp.model.Model;
import com.ls.drupalconapp.model.PreferencesManager;
import com.ls.drupalconapp.model.UpdateCallback;
import com.ls.drupalconapp.model.UpdatesManager;
import com.ls.ui.dialog.NoConnectionDialog;
import com.ls.util.L;
import com.ls.utils.AnalyticsManager;
import com.ls.utils.DialogHelper;
import com.ls.utils.NetworkUtils;

public class SplashActivity extends FragmentActivity {

    private static final int SPLASH_DURATION = 1500;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_splash);
        AnalyticsManager.sendEvent(this, "Application", R.string.action_open);

        initStatusBar();

        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean isOnline = NetworkUtils.isOn(SplashActivity.this);
                String lastUpdate = PreferencesManager.getInstance().getLastUpdateDate();

                if (isOnline) {
                    checkForUpdates();
                } else if (TextUtils.isEmpty(lastUpdate)) {
                    DialogHelper.showAllowStateLoss(SplashActivity.this, NoConnectionDialog.TAG, new NoConnectionDialog());
                } else {
                    startMainActivity();
                }
            }
        }, SPLASH_DURATION);
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    private void initStatusBar() {
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.primary_dark));
        }
    }

    private void checkForUpdates() {
        new AsyncTask<Void, Void, UpdatesManager>() {
            @Override
            protected UpdatesManager doInBackground(Void... params) {
                UpdatesManager manager = Model.instance().getUpdatesManager();
                manager.checkForDatabaseUpdate();
                return manager;
            }

            @Override
            protected void onPostExecute(UpdatesManager manager) {
                loadData(manager);
            }
        }.execute();

    }

    private void loadData(UpdatesManager manager) {
        manager.startLoading(new UpdateCallback() {
            @Override
            public void onDownloadSuccess() {
                L.d("onDownloadSuccess");
                startMainActivity();
            }

            @Override
            public void onDownloadError() {
                L.d("onDownloadError");
                DialogHelper.showAllowStateLoss(SplashActivity.this, NoConnectionDialog.TAG, new NoConnectionDialog());
            }
        });
    }

    private void startMainActivity() {
        HomeActivity.startThisActivity(this);
        finish();
    }
}
