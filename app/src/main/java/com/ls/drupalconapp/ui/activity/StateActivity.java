package com.ls.drupalconapp.ui.activity;

import com.ls.drupalconapp.modelV2.Model;
import com.ls.drupalconapp.modelV2.UpdatesManager;

import android.content.ComponentCallbacks2;

/**
 * Created by Yakiv M. on 26.09.2014.
 */
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
