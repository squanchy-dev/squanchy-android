package com.ls.drupalconapp.modelV2.managers;

import com.ls.drupal.AbstractBaseDrupalEntity;
import com.ls.drupal.DrupalClient;
import com.ls.drupalconapp.model.DatabaseManager;
import com.ls.drupalconapp.model.data.Speaker;
import com.ls.drupalconapp.modelV2.requests.SpeakersRequest;

import java.util.List;

public class SpeakerManager extends SynchronousItemManager<Speaker.Holder, Object, String> {

    public SpeakerManager(DrupalClient client) {
        super(client);
    }

    @Override
    protected AbstractBaseDrupalEntity getEntityToFetch(DrupalClient client, Object requestParams) {
        return new SpeakersRequest(client);
    }

    @Override
    protected String getEntityRequestTag(Object params) {
        return "speakers";
    }

    @Override
    protected boolean storeResponse(Speaker.Holder requestResponse, String tag) {
        DatabaseManager databaseManager = DatabaseManager.instance();
        List<Speaker> speakers = requestResponse.getSpeakers();

        if (speakers == null) {
            return false;
        }

        databaseManager.saveSpeakers(speakers);

        for (Speaker speaker : speakers) {
            if (speaker != null) {
                if (speaker.isDeleted()) {
                    databaseManager.deleteSpeaker(speaker);
                }
            }
        }

        return false;
    }
}
