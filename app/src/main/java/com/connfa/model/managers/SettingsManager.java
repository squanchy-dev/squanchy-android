package com.connfa.model.managers;

import android.content.Context;

import com.connfa.model.PreferencesManager;
import com.connfa.model.data.SettingsHolder;
import com.connfa.service.api.ConnfaRepository;

import io.reactivex.Observable;

public class SettingsManager extends SynchronousItemManager<SettingsHolder, String> {

    private final PreferencesManager preferencesManager;

    public SettingsManager(Context context) {
        super(context);
        this.preferencesManager = PreferencesManager.create(context);
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

    @Override
    protected Observable<SettingsHolder> doFetch(ConnfaRepository repository) {
        return repository.settings();
    }
}
