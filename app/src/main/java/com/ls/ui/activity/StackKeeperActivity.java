package com.ls.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

public class StackKeeperActivity extends StateActivity {

    private static final String EXTRAS_STATE = "EXTRAS_STATE";
    private static final String ACTION = "CHECK_STACK";

    private static final int MAX_ACTIVITIES_COUNT = 2;
    private static final int OPEN_STATE = 1;
    private static final int CLOSE_STATE = 2;

    private int mActivitiesCount = 0;
    private StackKeeperReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReceiver = new StackKeeperReceiver();

        sendMessage(OPEN_STATE);

        IntentFilter intentFilter = new IntentFilter(ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }

    @Override
    public void finish() {
        super.finish();
        sendMessage(CLOSE_STATE);
    }

    private void sendMessage(int state) {
        Intent intent = new Intent();
        intent.setAction(ACTION);
        intent.putExtra(EXTRAS_STATE, state);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private class StackKeeperReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int state = intent.getIntExtra(EXTRAS_STATE, OPEN_STATE);
            switch (state) {
                case OPEN_STATE:
                    mActivitiesCount++;
                    break;
                case CLOSE_STATE:
                    mActivitiesCount--;
            }

            if (mActivitiesCount >= MAX_ACTIVITIES_COUNT) {
                StackKeeperActivity.super.finish();
            }
        }
    }
}
