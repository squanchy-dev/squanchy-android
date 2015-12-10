package com.ls.ui.receiver;

import org.jetbrains.annotations.NotNull;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

public class ReceiverManager {
    public static final String EXTRAS_EVENT_ID = "EXTRAS_EVENT_ID";
    public static final String EXTRAS_IS_FAVORITE = "EXTRAS_IS_FAVORITE";
    public static final String ACTION_FAVORITE_UPDATED = "ACTION_FAVORITE_UPDATED";

    private FavoriteUpdatedListener favoriteUpdatedListener;
    private FavoriteReceiver favoriteReceiver;

    public ReceiverManager(@NotNull FavoriteUpdatedListener favoriteUpdatedListener) {
        this.favoriteUpdatedListener = favoriteUpdatedListener;
        favoriteReceiver = new FavoriteReceiver();
    }

    public void register(Context context) {
        IntentFilter filter = new IntentFilter(ACTION_FAVORITE_UPDATED);
        LocalBroadcastManager.getInstance(context).registerReceiver(favoriteReceiver, filter);
    }

    public void unregister(Context context) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(favoriteReceiver);
    }

    public static void updateFavorites(Context context, long eventId, boolean isFavorite) {
        Intent intent = new Intent(ACTION_FAVORITE_UPDATED);
        intent.putExtra(EXTRAS_EVENT_ID, eventId);
        intent.putExtra(EXTRAS_IS_FAVORITE, isFavorite);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public interface FavoriteUpdatedListener {
        void onFavoriteUpdated(long eventId, boolean isFavorite);
    }

    private class FavoriteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            long eventId = intent.getLongExtra(EXTRAS_EVENT_ID, -1);
            boolean isFavorite = intent.getBooleanExtra(EXTRAS_IS_FAVORITE, false);

            favoriteUpdatedListener.onFavoriteUpdated(eventId, isFavorite);
        }
    }

    public class ScreenOverlayReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
        }
    }

}
