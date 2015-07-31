package com.ls.ui.activity;

import com.ls.utils.ActivityManager;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by Yakiv M. on 26.09.2014.
 */
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
