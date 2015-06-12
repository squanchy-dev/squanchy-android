package com.ls.drupalconapp.ui.activity;

import com.ls.drupalconapp.R;
import com.ls.drupalconapp.model.DatabaseManager;
import com.ls.drupalconapp.model.Model;
import com.ls.drupalconapp.model.PreferencesManager;
import com.ls.drupalconapp.model.UpdateCallback;
import com.ls.drupalconapp.model.UpdatesManager;
import com.ls.drupalconapp.model.database.ILAPIDBFacade;
import com.ls.util.L;
import com.ls.utils.AnalyticsManager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import java.util.TimeZone;

public class SplashActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_splash);

        AnalyticsManager.sendEvent(this, "Application", R.string.action_open);

        checkForDatabaseUpdate();
        UpdatesManager manager = Model.instance().getUpdatesManager();
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

    private void checkForDatabaseUpdate() {
        DatabaseManager databaseManager = DatabaseManager.instance();
        ILAPIDBFacade facade = databaseManager.getFacade();
        facade.open();

        String timeZone = TimeZone.getDefault().getID();
        if (!TextUtils.isEmpty(timeZone)) {
            String prefTimeZone = PreferencesManager.getInstance().getTimeZoneId();
            if (!timeZone.equals(prefTimeZone)) {
                databaseManager.clearOldData();
                PreferencesManager.getInstance().saveLastUpdateDate("");
            }
            PreferencesManager.getInstance().saveTimeZoneId(timeZone);
        }

        facade.close();
    }
}
