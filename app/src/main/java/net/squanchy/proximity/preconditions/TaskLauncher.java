package net.squanchy.proximity.preconditions;

import android.content.Intent;
import android.content.IntentSender;

public interface TaskLauncher {

    void startActivityForResult(Intent intent, int requestCode);

    boolean permissionGranted(String permission);

    void requestPermissions(String[] permissions, int requestCode);

    void startIntentSenderForResult(IntentSender intentSender, int requestCode) throws IntentSender.SendIntentException;
}
