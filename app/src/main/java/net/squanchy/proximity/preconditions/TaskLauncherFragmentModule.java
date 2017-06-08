package net.squanchy.proximity.preconditions;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import dagger.Module;
import dagger.Provides;
import timber.log.Timber;

@Module
public class TaskLauncherFragmentModule {

    private final Fragment fragment;

    public TaskLauncherFragmentModule(Fragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    public TaskLauncher taskLauncher(Activity activity) {
        return new TaskLauncher() {
            @Override
            public void startActivityForResult(Intent intent, int requestCode) {
                fragment.startActivityForResult(intent, requestCode);
            }

            @Override
            public boolean permissionGranted(String permission) {
                return ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
            }

            @Override
            @TargetApi(Build.VERSION_CODES.M)
            public void requestPermissions(String[] permissions, int requestCode) {
                if (isMarshmallowOrLater()) {
                    fragment.requestPermissions(permissions, requestCode);
                } else {
                    Timber.e(new IllegalStateException("Trying to request runtime permission on Android pre-M"));
                }
            }

            @Override
            @TargetApi(Build.VERSION_CODES.N)
            public void startIntentSenderForResult(IntentSender intentSender, int requestCode) throws IntentSender.SendIntentException {
                if (isNougatOrLater()) {
                    fragment.startIntentSenderForResult(intentSender, requestCode, null, 0, 0, 0, Bundle.EMPTY);
                } else {
                    ActivityCompat.startIntentSenderForResult(activity, intentSender, requestCode, null, 0, 0, 0, Bundle.EMPTY);
                }
            }
        };
    }

    private static boolean isMarshmallowOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    private static boolean isNougatOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }
}
