package com.ls.drupalconapp.model;

import com.ls.drupal.DrupalClient;
import com.ls.drupalconapp.model.data.UpdateDate;
import com.ls.drupalconapp.model.database.ILAPIDBFacade;
import com.ls.drupalconapp.model.managers.SynchronousItemManager;
import com.ls.drupalconapp.ui.drawer.DrawerManager;
import com.ls.http.base.BaseRequest;
import com.ls.http.base.RequestConfig;
import com.ls.http.base.ResponseData;

import org.jetbrains.annotations.NotNull;

import android.os.AsyncTask;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by Yakiv M. on 19.09.2014.
 */
public class UpdatesManager {

    public static final int TYPES_REQUEST_ID = 1;
    public static final int LEVELS_REQUEST_ID = 2;
    public static final int TRACKS_REQUEST_ID = 3;
    public static final int SPEAKERS_REQUEST_ID = 4;
    public static final int LOCATIONS_REQUEST_ID = 5;
    public static final int HOUSE_PLANS_REQUEST_ID = 6;
    public static final int PROGRAMS_REQUEST_ID = 7;
    public static final int BOFS_REQUEST_ID = 8;
    public static final int SOCIALS_REQUEST_ID = 9;
    public static final int POIS_REQUEST_ID = 10;
    public static final int INFO_REQUEST_ID = 11;
    public static final int TWITTER_REQUEST_ID = 12;

    private DrupalClient mClient;

    public static final String IF_MODIFIED_SINCE_HEADER= "If-Modified-Since";
    public static final String LAST_MODIFIED_HEADER= "Last-Modified";

    public UpdatesManager(@NotNull DrupalClient client)
    {
        mClient = client;
    }

    public void startLoading(@NotNull final UpdateCallback callback) {
        new AsyncTask<Void,Void,Boolean>(){

            @Override
            protected Boolean doInBackground(Void... params) {
                return doPerformLoading();
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if(callback == null)
                {
                    return;
                }
                if(success)
                {
                    callback.onDownloadSuccess();
                }else{
                    callback.onDownloadError();
                }
            }
        }.execute();
    }

    public static int convertEventIdToEventModePos(int eventModePos) {
        switch (eventModePos) {
            case PROGRAMS_REQUEST_ID:
                return DrawerManager.EventMode.Program.ordinal();
            case BOFS_REQUEST_ID:
                return DrawerManager.EventMode.Bofs.ordinal();
            case SOCIALS_REQUEST_ID:
                return DrawerManager.EventMode.Social.ordinal();
        }
        return 0;
    }

    private boolean doPerformLoading()
    {
        RequestConfig config = new RequestConfig();
        config.setResponseFormat(BaseRequest.ResponseFormat.JSON);
        config.setRequestFormat(BaseRequest.RequestFormat.JSON);
        config.setResponseClassSpecifier(UpdateDate.class);
        BaseRequest checkForUpdatesRequest = new BaseRequest(BaseRequest.RequestMethod.GET,ApplicationConfig.BASE_URL+"checkUpdates",config);
        checkForUpdatesRequest.addRequestHeader(IF_MODIFIED_SINCE_HEADER, PreferencesManager.getInstance().getLastUpdateDate());
        ResponseData updatesData = mClient.performRequest(checkForUpdatesRequest, true);
        UpdateDate updateDate = (UpdateDate)updatesData.getData();

        if(updateDate == null)
        {
            return false;
        }
        updateDate.setTime(updatesData.getHeaders().get(LAST_MODIFIED_HEADER));
        return loadData(updateDate);
    }

    private boolean loadData(UpdateDate updateDate) {

        List<Integer> updateIds = updateDate.getIdsForUpdate();
        if(updateIds == null ||updateIds.isEmpty())
        {
            return true;
        }

        ILAPIDBFacade facade = DatabaseManager.instance().getFacade();
        synchronized (facade) {
            try {
                facade.open();
                facade.beginTransactions();
                boolean result = true;
                for (Integer i : updateIds){
                    result = sendRequestById(i);
                    if(!result)
                    {
                        break;
                    }
                }
                if (result) {
                    facade.setTransactionSuccesfull();
                    if (updateDate != null && !TextUtils.isEmpty(updateDate.getTime())) {
                        PreferencesManager.getInstance().saveLastUpdateDate(updateDate.getTime());
                    }
                }
                return result;
            } finally {
                facade.endTransactions();
                facade.close();
            }
        }

    }

    private boolean sendRequestById(int id){

        SynchronousItemManager manager = null;
        switch (id){
            case TYPES_REQUEST_ID:
                manager = Model.instance().getTypesManager();
                break;

            case LEVELS_REQUEST_ID:
                manager = Model.instance().getLevelsManager();
                break;

            case TRACKS_REQUEST_ID:
                manager = Model.instance().getTracksManager();
                break;

            case SPEAKERS_REQUEST_ID:
                manager = Model.instance().getSpeakerManager();
                break;

            case LOCATIONS_REQUEST_ID:
                manager = Model.instance().getLocationmanager();
                break;

//            case HOUSE_PLANS_REQUEST_ID:
//                loadHousePlans();
//                break;

            case PROGRAMS_REQUEST_ID:
                manager = Model.instance().getSessionsManager();
                break;

            case BOFS_REQUEST_ID:
                manager = Model.instance().getBofsManager();
                break;

            case SOCIALS_REQUEST_ID:
                manager = Model.instance().getSocialManager();
                break;

            case POIS_REQUEST_ID:
                manager = Model.instance().getPoisManager();
                break;

            case INFO_REQUEST_ID:
                manager = Model.instance().getInfoManager();
                break;

//            case TWITTER_REQUEST_ID:
//                loadTwitter();
//                break;

        }

        if(manager != null)
        {
            return manager.fetchData();
        }

        return false;
    }
}
