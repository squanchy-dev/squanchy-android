package com.ls.drupalconapp.model.managers;

import com.ls.drupal.AbstractBaseDrupalEntity;
import com.ls.drupal.DrupalClient;
import com.ls.drupalconapp.app.App;
import com.ls.drupalconapp.model.dao.EventDao;
import com.ls.drupalconapp.model.data.Event;

import java.util.List;

public class EventManager extends SynchronousItemManager<Event.Holder, Object, String> {

    protected EventDao mEventDao;

    public EventManager(DrupalClient client) {
        super(client);
        mEventDao = new EventDao(App.getContext());
    }

    @Override
    protected AbstractBaseDrupalEntity getEntityToFetch(DrupalClient client, Object requestParams) {
        return null;
    }

    @Override
    protected String getEntityRequestTag(Object params) {
        return null;
    }

    @Override
    protected boolean storeResponse(Event.Holder requestResponse, String tag) {
        return false;
    }

    public void saveEventSpeakers(Event data) {
        Long eventId = data.getId();
        List<Long> speakerEventIds = mEventDao.selectSpeakerEventIds();

        for (Long speakerId : data.getSpeakers()) {
            if (!speakerEventIds.contains(eventId)) {
                mEventDao.insertEventSpeaker(eventId, speakerId);
            }
        }
    }

    public void deleteEvent(Event data) {
        mEventDao.deleteDataSafe(data.getId());
        mEventDao.deleteEventAndSpeakerByEvent(data.getId());
    }

    public EventDao getEventDao() {
        return mEventDao;
    }

    public List<Long> getFavoriteEventDays() {
        return mEventDao.selectDistrictFavoriteDateSafe();
    }

    public void setFavoriteEvent(long eventId, boolean isFavorite) {
        mEventDao.setFavoriteSafe(eventId, isFavorite);
    }

    public void clear() {
        mEventDao.deleteAll();
    }
}
