package com.ls.utils;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by Yakiv M. on 26.09.2014.
 */
public class DialogHelper {

    public static void showAllowStateLoss(FragmentActivity activity, String tag,
            DialogFragment dialog) {
        if (activity != null && !activity.isFinishing()) {
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            ft.add(dialog, tag);
            ft.commitAllowingStateLoss();
        }
    }


    public static void dismissAllowStateLoss(FragmentActivity activity, DialogFragment dialog) {
        if (activity != null && !activity.isFinishing()) {
            dialog.dismissAllowingStateLoss();
        }
    }
}