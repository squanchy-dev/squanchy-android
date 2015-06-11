package com.ls.drupalconapp.model;

import com.ls.utils.L;

/**
 * Created by Yakiv M. on 20.09.2014.
 */
public abstract class UpdateCallback {

    abstract public void onDownloadSuccess();

    abstract public void onDownloadError();

    public void onUnexpectedBehavior(String reason) {
        L.e(reason);
    }
}
