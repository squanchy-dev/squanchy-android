package com.ls.drupalconapp.model.managers;

import com.ls.drupal.AbstractBaseDrupalEntity;
import com.ls.drupal.DrupalClient;
import com.ls.drupalconapp.model.data.Event;
import com.ls.drupalconapp.model.requests.SocialRequest;
import com.ls.drupalconapp.ui.adapter.item.EventListItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SocialManager extends EventManager {

    public SocialManager(DrupalClient client) {
        super(client);
    }

    @Override
    protected AbstractBaseDrupalEntity getEntityToFetch(DrupalClient client, Object requestParams) {
        return new SocialRequest(client);
    }

    @Override
    protected String getEntityRequestTag(Object params) {
        return "social";
    }

    @Override
    protected boolean storeResponse(Event.Holder requestResponse, String tag) {
        List<Event.Day> socials = requestResponse.getDays();
        if (socials == null) {
            return false;
        }

        List<Long> ids = mEventDao.selectFavoriteEventsSafe();
        SimpleDateFormat format = new SimpleDateFormat("d-MM-yyyy");

        for (Event.Day day : socials) {
            for (Event event : day.getEvents()) {
                try {
                    if (event != null) {
                        Date date = format.parse(day.getDate());
                        event.setDate(date);
                        event.setEventClass(Event.SOCIALS_CLASS);

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

    public List<Long> getSocialsDays() {
        return mEventDao.selectDistrictDateSafe(Event.SOCIALS_CLASS);
    }

    public List<EventListItem> getSocialItemsSafe(long day) {
        return mEventDao.selectSocialItemsSafe(Event.SOCIALS_CLASS, day);
    }
}