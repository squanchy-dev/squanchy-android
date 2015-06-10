package com.ls.drupalconapp.modelV2.managers;

import com.ls.drupal.AbstractBaseDrupalEntity;
import com.ls.drupal.DrupalClient;
import com.ls.drupalconapp.model.DatabaseManager;
import com.ls.drupalconapp.model.data.Event;
import com.ls.drupalconapp.modelV2.requests.SocialRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SocialManager extends SynchronousItemManager<Event.Holder, Object, String> {

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
        DatabaseManager databaseManager = DatabaseManager.instance();
        List<Long> ids = databaseManager.getFavoriteEvents();
        List<Event.Day> socials = requestResponse.getDays();

        if (socials == null) {
            return false;
        }

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

                        databaseManager.saveEvent(event);
                        databaseManager.saveEventSpeakers(event);

                        if (event.isDeleted()) {
                            databaseManager.deleteEvent(event);
                        }

                    }
                } catch (ParseException e) {
                }
            }
        }
        return true;
    }
}