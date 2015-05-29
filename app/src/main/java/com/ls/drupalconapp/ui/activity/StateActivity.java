package com.ls.drupalconapp.ui.activity;

import android.content.ComponentCallbacks2;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.ls.drupalconapp.model.DownloadManager;
import com.ls.drupalconapp.model.PreferencesManager;
import com.ls.drupalconapp.model.data.UpdateDate;
import com.ls.drupalconapp.model.http.HttpFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Yakiv M. on 26.09.2014.
 */
public class StateActivity extends BaseActivity {

    private static boolean wasInBackground = false;
//    protected ProgressBar progressBar;

    @Override
    public void onTrimMemory(int level) {
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            wasInBackground = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wasInBackground) {
            wasInBackground = false;
            checkForUpdates();
        }
    }

    private void checkForUpdates() {
        HttpFactory.createCheckUpdatesRequest(
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        onCheckUpdatesLoaded(jsonObject);
                    }
                }, null, PreferencesManager.getInstance().getLastUpdateDate()
        );
    }

    private void onCheckUpdatesLoaded(JSONObject jsonObject) {
        HttpFactory.isUpdating = true;
//        progressBar.setVisibility(View.VISIBLE);
        final UpdateDate updateDate = new Gson().fromJson(jsonObject.toString(), UpdateDate.class);

        int[] requestIds;

        try {
            JSONArray idsArray = jsonObject.getJSONArray("idsForUpdate");
            requestIds = new int[idsArray.length()];

            for (int i = 0; i<idsArray.length(); i++){
                requestIds[i] = idsArray.getInt(i);
            }

        } catch (JSONException e){
            requestIds = null;
        }

        DownloadManager downloadManager = new DownloadManager();
        downloadManager.loadDataForUpdate(StateActivity.this, requestIds, updateDate);
    }

    public static boolean wasInBackground() {
        return wasInBackground;
    }
}
