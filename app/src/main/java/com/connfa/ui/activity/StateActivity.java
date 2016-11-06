package com.connfa.ui.activity;

import android.content.ComponentCallbacks2;
import android.support.v7.app.AppCompatActivity;

import com.connfa.model.Model;
import com.connfa.model.UpdatesManager;

public abstract class StateActivity extends AppCompatActivity {

    private static boolean wasInBackground = false;

    @Override
    public void onTrimMemory(int level) {
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            wasInBackground = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wasInBackground) {
            wasInBackground = false;
            checkForUpdates();
        }
    }

    private void checkForUpdates() {
        UpdatesManager manager = Model.instance().getUpdatesManager();
        manager.startLoading(null);
    }
}
