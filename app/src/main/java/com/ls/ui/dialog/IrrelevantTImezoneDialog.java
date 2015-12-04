package com.ls.ui.dialog;

import com.ls.drupalcon.R;
import com.ls.drupalcon.model.PreferencesManager;
import com.ls.utils.DateUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.TimeZone;

public class IrrelevantTimezoneDialog extends DialogFragment {

    public static final String TAG = IrrelevantTimezoneDialog.class.getName();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        TimeZone eventTimeZone = PreferencesManager.getInstance().getServerTimeZoneObject();
        String timezoneNotificationDate = String.format(getActivity().getString(R.string.irrelevant_timezone_notificaiton),eventTimeZone.getDisplayName(),eventTimeZone.getID());

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(R.string.Attention);
        alertDialog.setMessage(timezoneNotificationDate);
        alertDialog.setPositiveButton(getActivity().getString(android.R.string.ok), null);

        return alertDialog.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    public static boolean isCurrentTimezoneRelevant()
    {
        TimeZone eventTimezone = DateUtils.getInstance().getTimeZone();
        TimeZone curentZone = TimeZone.getDefault();
        return curentZone.getID().equals(eventTimezone.getID());
    }
}

