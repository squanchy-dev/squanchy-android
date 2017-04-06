package net.squanchy.proximity.preconditions;

import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class TaskLauncherActivityModule {

    @Provides
    public TaskLauncher taskLauncher(AppCompatActivity activity) {
        return new TaskLauncher() {
            @Override
            public void startActivityForResult(Intent intent, int requestCode) {
                activity.startActivityForResult(intent, requestCode);
            }

            @Override
            public boolean permissionGranted(String permission) {
                return ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
            }

            @Override
            public void requestPermissions(String[] permissions, int requestCode) {
                ActivityCompat.requestPermissions(activity, permissions, requestCode);
            }

            @Override
            public void startIntentSenderForResult(IntentSender intentSender, int requestCode) throws IntentSender.SendIntentException {
                activity.startIntentSenderForResult(intentSender, requestCode, null, 0, 0, 0);
            }
        };
    }
}
