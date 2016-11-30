package com.connfa.model.managers;

import android.content.Context;

import com.ls.drupal.AbstractBaseDrupalEntity;
import com.ls.drupal.DrupalClient;
import com.ls.http.base.ResponseData;

public abstract class SynchronousItemManager<R, T> {

    private final Context context;
    private final DrupalClient client;

    public SynchronousItemManager(Context context, DrupalClient client) {
        this.context = context;
        this.client = client;
    }

    protected abstract AbstractBaseDrupalEntity getEntityToFetch(DrupalClient client);

    protected abstract T getEntityRequestTag();

    protected abstract boolean storeResponse(R requestResponse, T tag);

    public boolean fetchData() {
        AbstractBaseDrupalEntity request = getEntityToFetch(client);
        T tag = getEntityRequestTag();
        ResponseData response = request.pullFromServer(true, tag, null);

        int statusCode = response.getStatusCode();
        if (statusCode > 0 && statusCode < 400) {

            R responseObj = (R) response.getData();
            if (responseObj != null) {
                return storeResponse(responseObj, tag);
            }
        }

        return false;
    }

    public Context getContext() {
        return context;
    }

    public DrupalClient getClient() {
        return client;
    }
}
