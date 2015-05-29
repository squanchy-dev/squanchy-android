package com.ls.drupalconapp.model.http.utils;

import com.android.volley.ExecutorDelivery;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.ResponseDelivery;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.http.AndroidHttpClient;
import android.os.Build;

import java.io.File;
import java.util.concurrent.Executors;

public class RequestHelper {

    public static final String REQUEST_CACHE_PATH = "volley/request";
    public static final String IMAGE_CACHE_PATH = "volley/image";
    public static final int DEFAULT_POOL_SIZE = 8;
    public static final int DEFAULT_DISK_USAGE_BYTES = 50 * 1024 * 1024;

    public static RequestQueue newRedirectRequestQueue(Context context, DiskBasedCache cache) {
        RequestQueue queue = new RequestQueue(
                cache,
                RequestHelper.createNetwork(context, new RedirectHurlStack()),
                RequestHelper.DEFAULT_POOL_SIZE
        );
        queue.start();
        return queue;
    }

    public static File createCacheDir(Context context, String path) {
        // define cache folder
        File rootCache = context.getExternalCacheDir();
        if (rootCache == null) {
            rootCache = context.getCacheDir();
        }
        File cacheDir = new File(rootCache, path);
        cacheDir.mkdirs();
        return cacheDir;
    }

    public static File createInternalCacheDir(Context context, String path) {
        File cacheDir = new File(context.getCacheDir(), path);
        cacheDir.mkdir();
        return cacheDir;
    }

    public static ResponseDelivery createFixedThreadDelivery(int threadPoolSize) {
        // important part
        // int threadPoolSize = 10; // number of network dispatcher threads to create
        // pass Executor to constructor of ResponseDelivery object
        ResponseDelivery delivery = new ExecutorDelivery(
                Executors.newFixedThreadPool(threadPoolSize));
        return delivery;
    }

    public static Network createNetwork(Context context, HttpStack afterNineSDK) {
        HttpStack stack = null;
        if (Build.VERSION.SDK_INT >= 9) {
            stack = afterNineSDK;
        } else {
            stack = RequestHelper.createHttpStack(context);
        }

        return new BasicNetwork(stack);
    }

    public static HttpStack createHttpStack(Context context) {
        // Prior to Gingerbread, HttpUrlConnection was unreliable.
        // See: http://android-developers.blogspot.com/2011/09/androids-http-clients.html
        String userAgent = "volley/0";
        try {
            String packageName = context.getPackageName();
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
            userAgent = packageName + "/" + info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return new HttpClientStack(AndroidHttpClient.newInstance(userAgent));
    }
}
