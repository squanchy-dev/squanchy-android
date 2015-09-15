package com.ls.drupalcon.app;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.ls.drupal.DrupalClient;
import com.ls.drupalcon.BuildConfig;
import com.ls.drupalcon.model.AppDatabaseInfo;
import com.ls.drupalcon.model.Model;
import com.ls.drupalcon.model.PreferencesManager;
import com.ls.drupalcon.model.database.LAPIDBRegister;
import com.ls.ui.view.FontHelper;
import com.ls.http.base.BaseRequest;
import com.ls.util.image.DrupalImageView;

import io.fabric.sdk.android.Fabric;

public class App extends Application {
    private static Context mContext;
    private static final String PROPERTY_ID = "UA-267362-65";

    @Override
    public void onCreate() {
        super.onCreate();
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }

        mContext = getApplicationContext();

        LAPIDBRegister.getInstance().register(mContext, new AppDatabaseInfo(mContext));
        PreferencesManager.initializeInstance(mContext);
        Model.instance(mContext);
        FontHelper.init(mContext);
        DrupalImageView.setupSharedClient(new DrupalClient(null, Model.instance().createNewQueue(getApplicationContext()), BaseRequest.RequestFormat.JSON, null));
    }

    public static Context getContext() {
        return mContext;
    }

    public synchronized Tracker getTracker() {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
        return analytics.newTracker(PROPERTY_ID);
    }
}
