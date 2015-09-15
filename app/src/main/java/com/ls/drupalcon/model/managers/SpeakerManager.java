package com.ls.drupalcon.model.managers;

import com.ls.drupal.AbstractBaseDrupalEntity;
import com.ls.drupal.DrupalClient;
import com.ls.drupalcon.app.App;
import com.ls.drupalcon.model.dao.EventDao;
import com.ls.drupalcon.model.dao.SpeakerDao;
import com.ls.drupalcon.model.data.Speaker;
import com.ls.drupalcon.model.requests.SpeakersRequest;

import java.util.List;

public class SpeakerManager extends SynchronousItemManager<Speaker.Holder, Object, String> {

    private SpeakerDao mSpeakerDao;

    public SpeakerManager(DrupalClient client) {
        super(client);
        mSpeakerDao = new SpeakerDao(App.getContext());
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

    public List<Speaker> getSpeakersByEventId(long eventId) {
        return mSpeakerDao.getSpeakersByEventId(eventId);
    }

    public Speaker getSpeakerById(long id) {
        return mSpeakerDao.getSpeakerById(id).get(0);
    }

    public void clear() {
        mSpeakerDao.deleteAll();
    }
}
