package com.ls.drupalconapp.ui.dialog;


import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;

public class LoadingDialog extends DialogFragment {

    public static final String TAG = "LoadingDialog";

    private static final String ARG_MESSAGE = "ARG_MESSAGE";

    public static LoadingDialog newInstance(String message) {
        LoadingDialog dialog = new LoadingDialog();

        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, message);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String message = getArguments().getString(ARG_MESSAGE);
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(message != null ? message : "");

        return dialog;
    }
}
