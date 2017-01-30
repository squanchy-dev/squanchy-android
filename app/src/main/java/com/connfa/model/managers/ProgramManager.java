package com.connfa.model.managers;

import android.content.Context;

import com.connfa.model.PreferencesManager;
import com.connfa.model.data.Event;
import com.connfa.service.api.ConnfaRepository;
import com.connfa.ui.adapter.item.EventListItem;
import com.connfa.utils.DateUtils;

import java.util.Date;
import java.util.List;

import io.reactivex.Observable;

public class ProgramManager extends EventManager {

    PreferencesManager preferencesManager;

    public ProgramManager(Context context) {
        super(context);
        this.preferencesManager = PreferencesManager.create(context);
    }

    @Override
    protected String getEntityRequestTag() {
        return "sessions";
    }

    @Override
    protected boolean storeResponse(Event.Holder requestResponse, String tag) {
        List<Event.Day> sessions = requestResponse.getDays();
        if (sessions == null) {
            return false;
        }

        List<Long> ids = mEventDao.selectFavoriteEventsSafe();
        for (Event.Day day : sessions) {

            for (Event event : day.getEvents()) {
                if (event != null) {

                    Date date = DateUtils.convertEventDayDate(getContext(), day.getDate());
                    if (date != null) {
                        event.setDate(date);
                    }
                    event.setEventClass(Event.PROGRAM_CLASS);

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
        return repository.sessions();
    }

    public List<Long> getProgramDays() {
        List<Long> levelIds = preferencesManager.getExpLevels();
        List<Long> trackIds = preferencesManager.getTracks();

        if (levelIds.isEmpty() & trackIds.isEmpty()) {
            return mEventDao.selectDistrictDateSafe(Event.PROGRAM_CLASS);

        } else if (!levelIds.isEmpty() & !trackIds.isEmpty()) {
            return mEventDao.selectDistrictDateByTrackAndLevelIdsSafe(Event.PROGRAM_CLASS, levelIds, trackIds);

        } else if (!levelIds.isEmpty() & trackIds.isEmpty()) {
            return mEventDao.selectDistrictDateByLevelIdsSafe(Event.PROGRAM_CLASS, levelIds);

        } else {
            return mEventDao.selectDistrictDateByTrackIdsSafe(Event.PROGRAM_CLASS, trackIds);
        }
    }

    public List<EventListItem> getProgramItemsSafe(int eventClass, long day, List<Long> levelIds, List<Long> trackIds) {
        return mEventDao.selectProgramItemsSafe(eventClass, day, levelIds, trackIds);
    }

    public List<EventListItem> getFavoriteProgramItemsSafe(List<Long> favoriteEventIds, long day) {
        return mEventDao.selectFavoriteProgramItemsSafe(favoriteEventIds, day);
    }
}
