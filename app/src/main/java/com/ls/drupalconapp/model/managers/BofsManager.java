package com.ls.drupalconapp.model.managers;

import com.ls.drupal.AbstractBaseDrupalEntity;
import com.ls.drupal.DrupalClient;
import com.ls.drupalconapp.model.data.Event;
import com.ls.drupalconapp.model.requests.BofsRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BofsManager extends EventManager{

    public BofsManager(DrupalClient client) {
        super(client);
    }

    @Override
    protected AbstractBaseDrupalEntity getEntityToFetch(DrupalClient client, Object requestParams) {
        return new BofsRequest(client);
    }

    @Override
    protected String getEntityRequestTag(Object params) {
        return "bofs";
    }

    @Override
    protected boolean storeResponse(Event.Holder requestResponse, String tag) {
        List<Event.Day> bofs = requestResponse.getDays();
        if (bofs == null) {
            return false;
        }

        List<Long> ids = mEventDao.selectFavoriteEventsSafe();
        SimpleDateFormat format = new SimpleDateFormat("d-MM-yyyy");

        for (Event.Day day : bofs) {
            for (Event event : day.getEvents()) {
                try {
                    if (event != null) {
                        Date date = format.parse(day.getDate());
                        event.setDate(date);
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
                } catch (ParseException e) {
                }
            }
        }
        return true;
    }

    public List<Long> getBofsDays() {
        return mEventDao.selectDistrictDateSafe(Event.BOFS_CLASS);
    }
}
