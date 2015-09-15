package com.ls.drupalcon.model.managers;

import com.ls.drupal.AbstractBaseDrupalEntity;
import com.ls.drupal.DrupalClient;
import com.ls.drupalcon.model.data.Event;
import com.ls.drupalcon.model.requests.BofsRequest;
import com.ls.ui.adapter.item.EventListItem;
import com.ls.utils.DateUtils;

import java.util.Date;
import java.util.List;

public class BofsManager extends EventManager {

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
        for (Event.Day day : bofs) {

            for (Event event : day.getEvents()) {
                if (event != null) {

                    Date date = DateUtils.getInstance().convertEventDayDate(day.getDate());
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

    public List<Long> getBofsDays() {
        return mEventDao.selectDistrictDateSafe(Event.BOFS_CLASS);
    }

    public List<EventListItem> getBofsItemsSafe(long day) {
        return mEventDao.selectBofsItemsSafe(Event.BOFS_CLASS, day);
    }
}
