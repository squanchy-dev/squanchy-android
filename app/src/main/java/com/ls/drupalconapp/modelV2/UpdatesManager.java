package com.ls.drupalconapp.modelV2;

import com.ls.drupal.DrupalClient;
import com.ls.drupalconapp.model.DatabaseManager;
import com.ls.drupalconapp.model.PreferencesManager;
import com.ls.drupalconapp.model.data.UpdateDate;
import com.ls.drupalconapp.model.database.ILAPIDBFacade;
import com.ls.drupalconapp.model.http.HttpFactory;
import com.ls.drupalconapp.modelV2.managers.SynchronousItemManager;
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

    private UpdateDate mUpdateDate;
    private DrupalClient mClient;

    public UpdatesManager(@NotNull DrupalClient client)
    {
        mClient = client;
    }

    public void startLoading(@NotNull final DownloadCallback callback) {
        new AsyncTask<Void,Void,Boolean>(){

            @Override
            protected Boolean doInBackground(Void... params) {
                return doPerformLoading();
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if(success)
                {
                    callback.onDownloadSuccess();
                }else{
                    callback.onDownloadError();
                }
            }
        }.execute();
    }

    private boolean doPerformLoading()
    {
        RequestConfig config = new RequestConfig();
        config.setResponseFormat(BaseRequest.ResponseFormat.JSON);
        config.setResponseClassSpecifier(UpdateDate.class);
        BaseRequest checkForUpdatesRequest = new BaseRequest(BaseRequest.RequestMethod.GET,ApplicationConfig.BASE_URL+"checkUpdates",config);
        ResponseData updatesData = mClient.performRequest(checkForUpdatesRequest, true);
        UpdateDate updateDate = (UpdateDate)updatesData.getData();

        if(updateDate == null)
        {
            return false;
        }
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
            case HttpFactory.TYPES_REQUEST_ID:
                manager = Model.instance().getTypesManager();
                break;

            case HttpFactory.LEVELS_REQUEST_ID:
                manager = Model.instance().getLevelsManager();
                break;

            case HttpFactory.TRACKS_REQUEST_ID:
                manager = Model.instance().getTracksManager();
                break;

//            case HttpFactory.SPEAKERS_REQUEST_ID:
//                loadSpeakers();
//                break;
//
//            case HttpFactory.LOCATIONS_REQUEST_ID:
//                loadLocations();
//                break;
//
//            case HttpFactory.HOUSE_PLANS_REQUEST_ID:
//                loadHousePlans();
//                break;
//
//            case HttpFactory.PROGRAMS_REQUEST_ID:
//                loadPrograms();
//                break;
//
//            case HttpFactory.BOFS_REQUEST_ID:
//                loadBoFses();
//                break;
//
//            case HttpFactory.SOCIALS_REQUEST_ID:
//                loadSocials();
//                break;
//
//            case HttpFactory.POIS_REQUEST_ID:
//                loadPOIs();
//                break;
//
//            case HttpFactory.INFO_REQUEST_ID:
//                loadInfo();
//                break;
//
//            case HttpFactory.TWITTER_REQUEST_ID:
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
