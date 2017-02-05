package net.squanchy.ui.activity;

import android.content.ComponentCallbacks2;
import android.support.v7.app.AppCompatActivity;

import net.squanchy.model.Model;
import net.squanchy.model.UpdateCallback;
import net.squanchy.model.UpdatesManager;

import timber.log.Timber;

public abstract class StateActivity extends AppCompatActivity implements UpdateCallback {

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

    @Override
    protected void onPause() {
        super.onPause();
        Model.getInstance().getUpdatesManager().close();
    }

    private void checkForUpdates() {
        UpdatesManager manager = Model.getInstance().getUpdatesManager();
        manager.startLoading(this);
    }

    @Override
    public void onDownloadSuccess() {
        Timber.d("Update downloaded");
    }

    @Override
    public void onDownloadError() {
        Timber.d("Unable to download data update (see log above for error)");
    }
}
