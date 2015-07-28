package com.ls.drupalconapp.ui.activity;

import android.content.ComponentCallbacks2;

import com.ls.drupalconapp.model.Model;
import com.ls.drupalconapp.model.UpdatesManager;

public class StateActivity extends BaseActivity {

    private static boolean wasInBackground = false;
//    protected ProgressBar progressBar;

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

    public static boolean wasInBackground() {
        return wasInBackground;
    }
}
