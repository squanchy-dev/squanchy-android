package com.ls.drupalconapp.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

public class StackKeeperActivity extends StateActivity{
    private static final String ACTION = "CHECK_STACK";
    private static final String EXTRAS_STATE = "EXTRAS_STATE";

    private static final int OPEN_STATE = 1;
    private static final int CLOSE_STATE = 2;

    private int count = 0;
    private StackKeeperReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiver = new StackKeeperReceiver();

        sendMessage(OPEN_STATE);

        IntentFilter intentFilter = new IntentFilter(ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    public void finish() {
        super.finish();
        sendMessage(CLOSE_STATE);
    }

    private void sendMessage(int state){
        Intent intent = new Intent();
        intent.setAction(ACTION);
        intent.putExtra(EXTRAS_STATE, state);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private class StackKeeperReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            int state = intent.getIntExtra(EXTRAS_STATE, OPEN_STATE);

            if(state == OPEN_STATE)
                count++;
            else
                count --;

            if(count == 2)
                StackKeeperActivity.super.finish();
        }
    }
}
