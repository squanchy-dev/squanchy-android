package net.squanchy.proximity.preconditions;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import timber.log.Timber;

public final class TaskLauncherFactory {

    private TaskLauncherFactory() {
        // Not instantiable
    }

    public static TaskLauncher forFragment(Fragment fragment, Activity activity) {
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
            public void requestPermissions(String[] permissions, int requestCode) {
                if (isMarshmallowOrLater()) {
                    fragment.requestPermissions(permissions, requestCode);
                } else {
                    Timber.e(new IllegalStateException("Trying to request runtime permission on Android pre-M"));
                }
            }

            @Override
            public void startIntentSenderForResult(IntentSender intentSender, int requestCode) throws IntentSender.SendIntentException {
                if (isNougatOrLater()) {
                    fragment.startIntentSenderForResult(intentSender, requestCode, null, 0, 0, 0, Bundle.EMPTY);
                } else {
                    ActivityCompat.startIntentSenderForResult(activity, intentSender, requestCode, null, 0, 0, 0, Bundle.EMPTY);
                }
            }
        };
    }

    public static TaskLauncher forActivity(AppCompatActivity activity) {
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

    private static boolean isMarshmallowOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    private static boolean isNougatOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }
}
