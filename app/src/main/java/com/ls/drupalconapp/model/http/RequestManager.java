package com.ls.drupalconapp.model.http;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.DiskBasedCache;
import com.ls.drupalconapp.model.http.utils.RequestHelper;

import android.content.Context;

@Deprecated
public class RequestManager {

    private static RequestManager mInstance;
    private RequestQueue mQueue;

    private RequestManager(Context context) {
        Context applicationContext = context.getApplicationContext();

        DiskBasedCache cache = new DiskBasedCache(
                RequestHelper.createInternalCacheDir(context, RequestHelper.REQUEST_CACHE_PATH));
        mQueue = RequestHelper.newRedirectRequestQueue(applicationContext, cache);
    }

    public static synchronized void initializeWith(Context context) {
        if (mInstance == null) {
            mInstance = new RequestManager(context);
        }
    }

    public static synchronized RequestQueue queue() {
        if (mInstance == null) {
            throw new IllegalStateException(RequestManager.class.getSimpleName() +
                    " is not initialized, call initializeWith(..) method first.");
        }
        return mInstance.getQueue();
    }

    public RequestQueue getQueue() {
        return mQueue;
    }
}
