package com.ls.drupalconapp.model.managers;

import com.ls.drupal.AbstractBaseDrupalEntity;
import com.ls.drupal.DrupalClient;
import com.ls.drupalconapp.model.data.Event;
import com.ls.drupalconapp.model.requests.SessionsRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SessionsManager extends EventManager {

    public SessionsManager(DrupalClient client) {
        super(client);
    }

    @Override
    protected AbstractBaseDrupalEntity getEntityToFetch(DrupalClient client, Object requestParams) {
        return new SessionsRequest(client);
    }

    @Override
    protected String getEntityRequestTag(Object params) {
        return "sessions";
    }

    @Override
    protected boolean storeResponse(Event.Holder requestResponse, String tag) {
        List<Event.Day> sessions = requestResponse.getDays();
        if (sessions == null) {
            return false;
        }

        List<Long> ids = mEventDao.selectFavoriteEventsSafe();
        SimpleDateFormat format = new SimpleDateFormat("d-MM-yyyy");

        for (Event.Day day : sessions) {
            for (Event event : day.getEvents()) {
                try {
                    if (event != null) {
                        Date date = format.parse(day.getDate());
                        event.setDate(date);
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
                } catch (ParseException e) {
                }
            }
        }
        return true;
    }
}