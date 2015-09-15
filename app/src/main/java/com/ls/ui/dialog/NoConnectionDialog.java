package com.ls.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

import com.ls.drupalcon.R;

public class NoConnectionDialog extends DialogFragment {

    public static final String TAG = "NoConnectionDialog";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(R.string.Attention);
        alertDialog.setMessage(R.string.NoConnectionMessage);
        alertDialog.setPositiveButton(getActivity().getString(android.R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        return alertDialog.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        finish();
    }

    private void finish() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }
}
