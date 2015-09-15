package com.ls.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.ls.utils.ActivityManager;

public class BaseActivity extends ActionBarActivity {

    private ActivityManager mActivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityManager = new ActivityManager(this);
        mActivityManager.registerExitReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActivityManager.unregisterReceiver();
    }

}
