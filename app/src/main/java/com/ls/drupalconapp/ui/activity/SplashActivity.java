package com.ls.drupalconapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.ls.drupalconapp.R;
import com.ls.drupalconapp.model.DatabaseManager;
import com.ls.drupalconapp.model.DownloadCallback;
import com.ls.drupalconapp.model.DownloadManager;
import com.ls.drupalconapp.model.PreferencesManager;
import com.ls.drupalconapp.model.data.UpdateDate;
import com.ls.drupalconapp.model.database.ILAPIDBFacade;
import com.ls.drupalconapp.ui.dialog.NoConnectionDialog;
import com.ls.utils.AnalyticsManager;
import com.ls.utils.DialogHelper;
import com.ls.utils.NetworkUtils;

import java.util.TimeZone;

public class SplashActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_splash);

//        TextView txtLatin = (TextView) findViewById(R.id.txtLatin);
//        txtLatin.setPaintFlags(txtLatin.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
//
//        TextView txtAmerica = (TextView) findViewById(R.id.txtAmerica);
//        txtAmerica.setPaintFlags(txtAmerica.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
//
//        TextView txtDrupalCon = (TextView) findViewById(R.id.txtDrupalCon);
//        txtDrupalCon.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf"));
//
//        TextView txtMonth = (TextView) findViewById(R.id.txtMonthBegin);
//        txtMonth.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf"));

        AnalyticsManager.sendEvent(this, "Application", R.string.action_open);

        checkForDatabaseUpdate();
        DownloadManager downloadManager = new DownloadManager();
        downloadManager.startLoading(getBaseContext(), new DownloadCallback() {
            @Override
            public void onDownloadSuccess(UpdateDate updateDate) {
				if (!NetworkUtils.isOn(SplashActivity.this)) {
					DialogHelper.showAllowStateLoss(SplashActivity.this, NoConnectionDialog.TAG, new NoConnectionDialog());
					return;
				}

				if (updateDate != null && !TextUtils.isEmpty(updateDate.getTime())) {
					PreferencesManager.getInstance().saveLastUpdateDate(updateDate.getTime());
				}

				startMainActivity();
			}

            @Override
            public void onDownloadError() {
                if (PreferencesManager.getInstance().getLastUpdateDate().equals("") && !isFinishing()) {
                    DialogHelper.showAllowStateLoss(
                            SplashActivity.this,
                            NoConnectionDialog.TAG,
                            new NoConnectionDialog()
                    );
                } else if (!isFinishing()) {
                    startMainActivity();
                } else {
                    finish();
                }
            }
        });
    }

    private void startMainActivity() {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void checkForDatabaseUpdate() {
        DatabaseManager databaseManager = new DatabaseManager(SplashActivity.this);
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
