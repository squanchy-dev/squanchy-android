package com.connfa.model.managers;

import android.content.Context;

import com.connfa.model.PreferencesManager;
import com.connfa.model.data.Event;
import com.connfa.service.ConnfaRepository;
import com.connfa.ui.adapter.item.EventListItem;
import com.connfa.utils.DateUtils;
import com.ls.drupal.DrupalClient;

import java.util.Date;
import java.util.List;

import io.reactivex.Observable;

public class BofsManager extends EventManager {

    public BofsManager(Context context, DrupalClient client) {
        super(context, client);
    }

    @Override
    protected String getEntityRequestTag() {
        return "bofs";
    }

    @Override
    protected boolean storeResponse(Event.Holder requestResponse, String tag) {
        List<Event.Day> bofs = requestResponse.getDays();
        if (bofs == null) {
            return false;
        }

        List<Long> ids = mEventDao.selectFavoriteEventsSafe();
        for (Event.Day day : bofs) {

            for (Event event : day.getEvents()) {
                if (event != null) {

                    Date date = DateUtils.convertEventDayDate(getContext(), day.getDate());
                    if (date != null) {
                        event.setDate(date);
                    }
                    event.setEventClass(Event.BOFS_CLASS);

                    for (long id : ids) {
                        if (event.getId() == id) {
                            event.setFavorite(true);
                            break;
                        }
                    }

                    mEventDao.saveOrUpdateSafe(event);
                    saveEventSpeakers(event);

                    if (event.isDeleted()) {
                        deleteEvent(event);
                    }

                }
            }
        }
        return true;
    }

    @Override
    protected Observable<Event.Holder> doFetch(ConnfaRepository repository) {
        return repository.bofs();
    }

    public List<Long> getBofsDays() {
        PreferencesManager preferencesManager = PreferencesManager.create(getContext());
        List<Long> levelIds = preferencesManager.getExpLevels();
        List<Long> trackIds = preferencesManager.getTracks();

        if (levelIds.isEmpty() && trackIds.isEmpty()) {
            return mEventDao.selectDistrictDateSafe(Event.BOFS_CLASS);
        }

        if (!levelIds.isEmpty() && !trackIds.isEmpty()) {
            return mEventDao.selectDistrictDateByTrackAndLevelIdsSafe(Event.BOFS_CLASS, levelIds, trackIds);
        }

        if (!levelIds.isEmpty() && trackIds.isEmpty()) {
            return mEventDao.selectDistrictDateByLevelIdsSafe(Event.BOFS_CLASS, levelIds);

        }
        return mEventDao.selectDistrictDateByTrackIdsSafe(Event.BOFS_CLASS, trackIds);
    }

    public List<EventListItem> getBofsItemsSafe(long day) {
        return mEventDao.selectBofsItemsSafe(Event.BOFS_CLASS, day);
    }
}
