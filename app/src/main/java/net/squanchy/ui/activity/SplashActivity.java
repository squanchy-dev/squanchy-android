package net.squanchy.ui.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import net.squanchy.R;
import net.squanchy.analytics.Analytics;
import net.squanchy.model.Model;
import net.squanchy.model.PreferencesManager;
import net.squanchy.model.UpdateCallback;
import net.squanchy.model.UpdatesManager;
import net.squanchy.ui.dialog.NoConnectionDialog;
import net.squanchy.utils.NetworkUtils;
import com.ls.util.L;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 1500;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_splash);

        Analytics.from(this)
                .trackEvent("Application", getString(R.string.action_open));

        mHandler = new Handler();
        mHandler.postDelayed(() -> startSplash(), SPLASH_DURATION);
    }

    @Override
    protected void onDestroy() {
        Model.getInstance().getUpdatesManager().close();
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    private void startSplash() {
        String lastUpdate = PreferencesManager.create(this).getLastUpdateDate();
        boolean isOnline = NetworkUtils.isOn(SplashActivity.this);

        if (isOnline) {
            checkForUpdates();
        } else if (TextUtils.isEmpty(lastUpdate)) {
            showNoNetworkDialog();
        } else {
            startMainActivity();
        }
    }

    private void checkForUpdates() {
        new AsyncTask<Void, Void, UpdatesManager>() {
            @Override
            protected UpdatesManager doInBackground(Void... params) {
                UpdatesManager manager = Model.getInstance().getUpdatesManager();
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
                showNoNetworkDialog();
            }
        });
    }

    private void startMainActivity() {
        HomeActivity.startThisActivity(this);
        finish();
    }

    private void showNoNetworkDialog() {
        if (!isFinishing()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(new NoConnectionDialog(), NoConnectionDialog.TAG);
            ft.commitAllowingStateLoss();
        }
    }
}
