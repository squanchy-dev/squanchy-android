package com.ls.drupalconapp.modelV2;

import com.ls.drupalconapp.model.data.UpdateDate;
import com.ls.utils.L;

/**
 * Created by Yakiv M. on 20.09.2014.
 */
public abstract class DownloadCallback {

    abstract public void onDownloadSuccess();

    abstract public void onDownloadError();

    public void onUnexpectedBehavior(String reason) {
        L.e(reason);
    }
}
