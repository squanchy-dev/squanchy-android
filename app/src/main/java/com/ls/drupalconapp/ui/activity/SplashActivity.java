package com.ls.drupalconapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.ls.drupalconapp.R;
import com.ls.drupalconapp.model.Model;
import com.ls.drupalconapp.model.UpdateCallback;
import com.ls.drupalconapp.model.UpdatesManager;
import com.ls.util.L;
import com.ls.utils.AnalyticsManager;

public class SplashActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_splash);

        AnalyticsManager.sendEvent(this, "Application", R.string.action_open);

        UpdatesManager manager = Model.instance().getUpdatesManager();
        manager.checkForDatabaseUpdate();

        manager.startLoading(new UpdateCallback() {
            @Override
            public void onDownloadSuccess() {
                L.e("onDownloadSuccess");
                startMainActivity();
            }

            @Override
            public void onDownloadError() {
                L.e("onDownloadError");
            }
        });
    }

    private void startMainActivity() {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
