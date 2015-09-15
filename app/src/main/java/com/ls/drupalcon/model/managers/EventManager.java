package com.ls.drupalcon.model.managers;

import com.ls.drupal.AbstractBaseDrupalEntity;
import com.ls.drupal.DrupalClient;
import com.ls.drupalcon.app.App;
import com.ls.drupalcon.model.dao.EventDao;
import com.ls.drupalcon.model.data.Event;
import com.ls.drupalcon.model.data.EventDetailsEvent;
import com.ls.drupalcon.model.data.TimeRange;

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

    public EventDetailsEvent getEventById(long id) {
        return mEventDao.getEventById(id);
    }

    public List<TimeRange> getDistrictTimeRangeSafe(int eventClass, long day) {
        return mEventDao.selectDistrictTimeRangeSafe(eventClass, day);
    }

    public List<TimeRange> getDistrictTimeRangeSafe(int eventClass, long day, List<Long> levelIds, List<Long> trackIds) {
        return mEventDao.selectDistrictTimeRangeByLevelTrackIdsSafe(eventClass, day, levelIds, trackIds);
    }

    public List<Long> getEventSpeakerSafe(long id) {
        return mEventDao.selectEventSpeakersSafe(id);
    }

    public List<Event> getEventsByIdsAndDaySafe(long day) {
        FavoriteManager favoriteManager = new FavoriteManager();
        List<Long> favoriteEventIds = favoriteManager.getFavoriteEventsSafe();
        return mEventDao.selectEventsByIdsAndDaySafe(favoriteEventIds, day);
    }

    public void clear() {
        mEventDao.deleteAll();
    }

    public EventDao getEventDao() {
        return mEventDao;
    }
}
