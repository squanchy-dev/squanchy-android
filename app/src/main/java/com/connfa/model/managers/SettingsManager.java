package com.connfa.model.managers;

import android.content.Context;

import com.connfa.model.PreferencesManager;
import com.connfa.model.data.SettingsHolder;
import com.connfa.model.requests.SettingsRequest;
import com.ls.drupal.AbstractBaseDrupalEntity;
import com.ls.drupal.DrupalClient;

public class SettingsManager extends SynchronousItemManager<SettingsHolder, String> {

    private final PreferencesManager preferencesManager;

    public SettingsManager(Context context, DrupalClient client) {
        super(context, client);
        this.preferencesManager = PreferencesManager.create(context);
    }

    @Override
    protected AbstractBaseDrupalEntity getEntityToFetch(DrupalClient client) {
        return new SettingsRequest(getContext(), client);
    }

    @Override
    protected String getEntityRequestTag() {
        return "settings";
    }

    @Override
    protected boolean storeResponse(SettingsHolder requestResponse, String tag) {
        String timeZone = requestResponse.getSettings().getTimeZone();
        preferencesManager.saveTimeZone(timeZone);
        String searchQuery = requestResponse.getSettings().getTwitterSearchQuery();
        preferencesManager.saveTwitterSearchQuery(searchQuery);
        return true;
    }
}
