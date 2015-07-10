package com.ls.drupalconapp.model.managers;

import com.ls.drupal.AbstractBaseDrupalEntity;
import com.ls.drupal.DrupalClient;
import com.ls.drupalconapp.model.PreferencesManager;
import com.ls.drupalconapp.model.data.SettingsHolder;
import com.ls.drupalconapp.model.requests.SettingsRequest;

public class SettingsManager extends SynchronousItemManager<SettingsHolder, Object, String> {

    public SettingsManager(DrupalClient client) {
        super(client);
    }

    @Override
    protected AbstractBaseDrupalEntity getEntityToFetch(DrupalClient client, Object requestParams) {
        return new SettingsRequest(client);
    }

    @Override
    protected String getEntityRequestTag(Object params) {
        return "settings";
    }

    @Override
    protected boolean storeResponse(SettingsHolder requestResponse, String tag) {
        String timeZoneNumber = requestResponse.getSettings().getTimeZone();
        String timeZone = String.format("GMT%s", timeZoneNumber);
        PreferencesManager.getInstance().saveTimeZone(timeZone);

        return true;
    }
}
