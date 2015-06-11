package com.ls.drupalconapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;

import com.ls.drupalconapp.R;
import com.ls.drupalconapp.model.DatabaseManager;
import com.ls.drupalconapp.model.PreferencesManager;
import com.ls.drupalconapp.model.database.ILAPIDBFacade;
import com.ls.drupalconapp.modelV2.DownloadCallback;
import com.ls.drupalconapp.modelV2.Model;
import com.ls.drupalconapp.modelV2.UpdatesManager;
import com.ls.utils.AnalyticsManager;

import java.util.TimeZone;

public class SplashActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_splash);

        AnalyticsManager.sendEvent(this, "Application", R.string.action_open);

        checkForDatabaseUpdate();
//        DownloadManager downloadManager = new DownloadManager();
//        downloadManager.startLoading(getBaseContext(), new DownloadCallback() {
//            @Override
//            public void onDownloadSuccess(UpdateDate updateDate) {
//				if (!NetworkUtils.isOn(SplashActivity.this)) {
//					DialogHelper.showAllowStateLoss(SplashActivity.this, NoConnectionDialog.TAG, new NoConnectionDialog());
//					return;
//				}
//
//				if (updateDate != null && !TextUtils.isEmpty(updateDate.getTime())) {
//					PreferencesManager.getInstance().saveLastUpdateDate(updateDate.getTime());
//				}
//
//				startMainActivity();
//			}
//
//            @Override
//            public void onDownloadError() {
//                if (PreferencesManager.getInstance().getLastUpdateDate().equals("") && !isFinishing()) {
//                    DialogHelper.showAllowStateLoss(
//                            SplashActivity.this,
//                            NoConnectionDialog.TAG,
//                            new NoConnectionDialog()
//                    );
//                } else if (!isFinishing()) {
//                    startMainActivity();
//                } else {
//                    finish();
//                }
//            }
//        });

        UpdatesManager manager = Model.instance().getUpdatesManager();
        manager.startLoading(new DownloadCallback() {
            @Override
            public void onDownloadSuccess() {
                Log.e("TEST", "onDownloadSuccess");
                startMainActivity();
            }

            @Override
            public void onDownloadError() {
                Log.e("TEST", "onDownloadError");
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
