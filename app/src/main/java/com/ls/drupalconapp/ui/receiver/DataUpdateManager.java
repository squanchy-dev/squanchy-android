package com.ls.drupalconapp.ui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Kuhta on 26.09.2014.
 */
public class DataUpdateManager {
	public static final String ACTION_DATA_UPDATED = "ACTION_DATA_UPDATED";
    public static final String ACTION_DATA_EXTRA = "ACTION_DATA_EXTRA";

	private DataUpdatedListener dataUpdatedListener;
	private DataUpdateReceiver dataUpdateReceiver;

	public DataUpdateManager(@NotNull DataUpdatedListener dataUpdatedListener){
		this.dataUpdatedListener = dataUpdatedListener;
		dataUpdateReceiver = new DataUpdateReceiver();
	}

	public void register(Context context) {
		IntentFilter filter = new IntentFilter(ACTION_DATA_UPDATED);
		LocalBroadcastManager.getInstance(context).registerReceiver(dataUpdateReceiver, filter);
	}

	public void unregister(Context context) {
		LocalBroadcastManager.getInstance(context).unregisterReceiver(dataUpdateReceiver);
	}

	public static void updateData(Context context, int[] requestIds) {
		Intent intent = new Intent(ACTION_DATA_UPDATED);
        intent.putExtra(ACTION_DATA_EXTRA, requestIds);
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}

	public static interface DataUpdatedListener{
		public void onDataUpdated(int[] requestIds);
	}

	private class DataUpdateReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
            int [] requestIds = intent.getIntArrayExtra(ACTION_DATA_EXTRA);
            dataUpdatedListener.onDataUpdated(requestIds);
		}
	}
}
