package com.ls.drupalconapp.app;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.ls.drupalconapp.R;
import com.ls.drupalconapp.model.AppDatabaseInfo;
import com.ls.drupalconapp.model.DatabaseManager;
import com.ls.drupalconapp.model.PreferencesManager;
import com.ls.drupalconapp.model.database.LAPIDBRegister;
import com.ls.drupalconapp.model.http.ImageManager;
import com.ls.drupalconapp.model.http.RequestManager;
import com.ls.drupalconapp.modelV2.Model;
import com.ls.drupalconapp.ui.view.FontHelper;

import java.util.HashMap;

public class App extends Application {
    private static Context mContext;
    private static final String PROPERTY_ID = "UA-267362-65";

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();

        LAPIDBRegister.getInstance().register(mContext, new AppDatabaseInfo(mContext));
        PreferencesManager.initializeInstance(mContext);
        RequestManager.initializeWith(mContext);
        ImageManager.initializeWith(mContext);
        DatabaseManager.instance(mContext);
        Model.instance(mContext);
        FontHelper.init(mContext);
    }

    public static Context getContext() {
        return mContext;
    }

    public synchronized Tracker getTracker() {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
        return analytics.newTracker(PROPERTY_ID);
    }
}
