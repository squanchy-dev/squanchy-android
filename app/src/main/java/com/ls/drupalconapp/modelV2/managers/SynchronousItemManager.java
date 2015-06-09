package com.ls.drupalconapp.modelV2.managers;

import com.ls.drupal.AbstractBaseDrupalEntity;
import com.ls.drupal.DrupalClient;
import com.ls.http.base.ResponseData;

import android.os.AsyncTask;

import java.net.HttpURLConnection;

/**
 * Created on 08.06.2015.
 */
public abstract class SynchronousItemManager<FetchRequestResponseToManage ,ParametersClass,TagClass> {
    private DrupalClient client;

    protected abstract AbstractBaseDrupalEntity getEntityToFetch(DrupalClient client, ParametersClass requestParams);

    protected abstract TagClass getEntityRequestTag(ParametersClass params);

    protected abstract boolean storeResponse(FetchRequestResponseToManage requestResponse, TagClass tag);

    public SynchronousItemManager(DrupalClient client) {
        this.client = client;
    }

    public boolean fetchData(ParametersClass requestParams) {
        AbstractBaseDrupalEntity request = getEntityToFetch(this.client, requestParams);
        TagClass tag = getEntityRequestTag(requestParams);
        ResponseData response = request.pullFromServer(true, tag, null);
        FetchRequestResponseToManage responseObj = (FetchRequestResponseToManage)response.getData();
        if(responseObj != null) {
            return storeResponse(responseObj, tag);
        }
        return false;
    }

    public boolean fetchData() {
        return fetchData(null);
    }
}
