package net.squanchy.model.managers;

import android.content.Context;

import net.squanchy.model.dao.EventDao;
import net.squanchy.model.data.Event;
import net.squanchy.model.data.EventDetailsEvent;
import net.squanchy.model.data.TimeRange;
import net.squanchy.service.api.ConnfaRepository;

import java.util.List;

import io.reactivex.Observable;

public class EventManager extends SynchronousItemManager<Event.Holder, String> {

    protected EventDao mEventDao;

    public EventManager(Context context) {
        super(context);
        mEventDao = new EventDao(context);
    }

    @Override
    protected String getEntityRequestTag() {
        return null;
    }

    @Override
    protected boolean storeResponse(Event.Holder requestResponse, String tag) {
        return false;
    }

    @Override
    protected Observable<Event.Holder> doFetch(ConnfaRepository repository) {
        throw new UnsupportedOperationException("You can't do this.");
    }

    void saveEventSpeakers(Event data) {
        Long eventId = data.getId();
        List<Long> speakerEventIds = mEventDao.selectEventSpeakersSafe(eventId);

        for (Long speakerId : data.getSpeakers()) {
            if (!speakerEventIds.contains(speakerId)) {
                mEventDao.insertEventSpeaker(eventId, speakerId);
            }

            speakerEventIds.remove(speakerId);
        }

        //Delete removed speakers
        for (Long speakerId : speakerEventIds) {
            mEventDao.deleteByEventAndSpeaker(eventId, speakerId);
        }
    }

    void deleteEvent(Event data) {
        mEventDao.deleteDataSafe(data.getId());
        mEventDao.deleteEventAndSpeakerByEvent(data.getId());
        mEventDao.setFavoriteSafe(data.getId(), false);
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

    public List<TimeRange> getDistrictFavoriteTimeRangeSafe(int eventClass, List<Long> favoriteEventIds, long day) {
        return mEventDao.selectDistrictFavTimeRangeSafe(eventClass, favoriteEventIds, day);
    }

    public List<Long> getEventSpeakerSafe(long id) {
        return mEventDao.selectEventSpeakersSafe(id);
    }

    public void clear() {
        mEventDao.deleteAll();
    }

    public EventDao getEventDao() {
        return mEventDao;
    }
}
