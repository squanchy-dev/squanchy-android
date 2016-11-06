package com.connfa.model.managers;

import android.content.Context;

import com.connfa.model.dao.EventDao;
import com.connfa.model.dao.SpeakerDao;
import com.connfa.model.data.Speaker;
import com.connfa.model.requests.SpeakersRequest;
import com.ls.drupal.AbstractBaseDrupalEntity;
import com.ls.drupal.DrupalClient;

import java.util.List;

public class SpeakerManager extends SynchronousItemManager<Speaker.Holder, Object, String> {

    private final Context mContext;

    private SpeakerDao mSpeakerDao;

    public SpeakerManager(DrupalClient client, Context context) {
        super(client);
        this.mContext = context;
        this.mSpeakerDao = new SpeakerDao(context);
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

        EventDao eventDao = new EventDao(mContext);
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
        List<Speaker> speakerById = mSpeakerDao.getSpeakerById(id);
        if (!speakerById.isEmpty()) {
            return speakerById.get(0);
        } else {
            return null;
        }
    }

    public void clear() {
        mSpeakerDao.deleteAll();
    }
}
