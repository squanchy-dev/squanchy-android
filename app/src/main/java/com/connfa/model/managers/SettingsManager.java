package com.connfa.model.managers;

import com.ls.drupal.AbstractBaseDrupalEntity;
import com.ls.drupal.DrupalClient;
import com.connfa.model.PreferencesManager;
import com.connfa.model.data.SettingsHolder;
import com.connfa.model.requests.SettingsRequest;
import com.connfa.utils.DateUtils;

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
//        String timeZoneNumber = requestResponse.getSettings().getTimeZone();
//        String timeZone = String.format("GMT%s", timeZoneNumber);
        String timeZone = requestResponse.getSettings().getTimeZone();
        PreferencesManager.getInstance().saveTimeZone(timeZone);
        DateUtils.getInstance().setTimezone(timeZone);
        String searchQuery = requestResponse.getSettings().getTwitterSearchQuery();
        PreferencesManager.getInstance().saveTwitterSearchQuery(searchQuery);
        return true;
    }
}
