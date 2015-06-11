package com.ls.drupalconapp.model.managers;

import com.ls.drupal.AbstractBaseDrupalEntity;
import com.ls.drupal.DrupalClient;
import com.ls.drupalconapp.model.PreferencesManager;
import com.ls.drupalconapp.model.data.InfoItem;
import com.ls.drupalconapp.model.requests.InfoRequest;

public class InfoManager extends SynchronousItemManager<InfoItem.General, Object, String> {

    public InfoManager(DrupalClient client) {
        super(client);
    }

    @Override
    protected AbstractBaseDrupalEntity getEntityToFetch(DrupalClient client, Object requestParams) {
        return new InfoRequest(client);
    }

    @Override
    protected String getEntityRequestTag(Object params) {
        return "info";
    }

    @Override
    protected boolean storeResponse(InfoItem.General requestResponse, String tag) {
        PreferencesManager.getInstance().saveMajorInfoTitle(requestResponse.getMajorTitle());
        PreferencesManager.getInstance().saveMinorInfoTitle(requestResponse.getMinorTitle());

        return true;
    }
}