package com.ls.drupalconapp.modelV2;

import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.ls.drupal.DrupalClient;
import com.ls.drupalconapp.modelV2.managers.HURLCookieStore;
import com.ls.drupalconapp.modelV2.managers.LoginManager;
import com.ls.http.base.BaseRequest;
import com.ls.http.base.ResponseData;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;

/**
 * Created on 21.05.2015.
 */
public class Model {

    private static Model instance;
    public static Model instance(Context theContext)
    {
        if (instance == null)
        {
            instance = new Model(theContext);
        }

        return instance;
    }


    public static Model instance()
    {
        if (instance == null)
        {
            throw new IllegalStateException("Called method on uninitialized model");
        }

        return instance;
    }

    private DrupalClient client;
    private LoginManager loginManager;
    private CookieStore cookieStore;
    private RequestQueue queue;


    public DrupalClient getClient() {
        return client;
    }

    public RequestQueue getQueue() {
        return queue;
    }

    public LoginManager getLoginManager() {
        return loginManager;
    }

    public CookieStore getCookieStore() {
        return cookieStore;
    }

    /**
     * NOTE: login is performed in synchroneus way so you must never call it from UI thread.
     * @param userName
     * @param password
     * @return
     */
    public ResponseData performLogin(String userName, String password)
    {
        return this.loginManager.login(userName,password,queue);
    }


    private Model(Context context)
    {
        loginManager = new LoginManager();
        queue = createNewQueue(context);
        client = new DrupalClient(ApplicationConfig.BASE_URL,queue, BaseRequest.RequestFormat.JSON,loginManager);

    }


    //Initialization

    private RequestQueue createNewQueue(Context context)
    {
        cookieStore = new HURLCookieStore(context);
        CookieManager cmrCookieMan = new CookieManager(cookieStore, CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cmrCookieMan);

        HttpStack stack;

        String userAgent = "volley/0";
        try {
            String packageName = context.getPackageName();
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
            userAgent = packageName + "/" + info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }

        if (Build.VERSION.SDK_INT >= 9) {
            stack = new HurlStack();

        } else {
            stack = new HttpClientStack(AndroidHttpClient.newInstance(userAgent));
        }

        return newRequestQueue(context, stack);
    }

    /**
     * volley's default implementation uses internal cache only so we've implemented our, allowing external cache usage.
     * @param context
     * @param stack
     * @return
     */
    private static RequestQueue newRequestQueue(Context context, HttpStack stack) {

        File cacheDir;

        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            cacheDir = new File(context.getExternalCacheDir(), "volley");
        } else {
            cacheDir = new File(context.getCacheDir(), "volley");
        }

        if (stack == null) {
            stack = new HurlStack();
        }

        Network network = new BasicNetwork(stack);

        RequestQueue queue = new RequestQueue(new DiskBasedCache(cacheDir, ApplicationConfig.CACHE_DISK_USAGE_BYTES), network,1);
        queue.start();

        return queue;
    }



}
