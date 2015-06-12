package com.ls.drupalconapp.model.managers;

import com.ls.drupal.AbstractBaseDrupalEntity;
import com.ls.drupal.DrupalClient;
import com.ls.drupalconapp.app.App;
import com.ls.drupalconapp.model.dao.EventDao;
import com.ls.drupalconapp.model.dao.SpeakerDao;
import com.ls.drupalconapp.model.data.Speaker;
import com.ls.drupalconapp.model.requests.SpeakersRequest;

import java.util.List;

public class SpeakerManager extends SynchronousItemManager<Speaker.Holder, Object, String> {

    private SpeakerDao mSpeakerDao;

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
        List<Speaker> speakers = requestResponse.getSpeakers();
        if (speakers == null) {
            return false;
        }

        EventDao eventDao = new EventDao(App.getContext());
        mSpeakerDao = new SpeakerDao(App.getContext());
        mSpeakerDao.saveOrUpdateDataSafe(speakers);

        for (Speaker speaker : speakers) {
            if (speaker != null) {
                if (speaker.isDeleted()) {
                    mSpeakerDao.deleteDataSafe(speaker.getId());
                    eventDao.deleteEventAndSpeakerBySpeaker(speaker.getId());
                }
            }
        }

        return true;
    }

    public List<Speaker> getSpeakers() {
        return mSpeakerDao.selectSpeakersOrderedByName();
    }

    public List<Speaker> getSpeakers(long speakerId) {
        return mSpeakerDao.getDataSafe(speakerId);
    }
}
