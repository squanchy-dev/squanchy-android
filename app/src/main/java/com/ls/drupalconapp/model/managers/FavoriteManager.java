package com.ls.drupalconapp.model.managers;

import com.ls.drupalconapp.app.App;
import com.ls.drupalconapp.model.dao.EventDao;

import java.util.List;

public class FavoriteManager {

    private EventDao mEventDao;

    public FavoriteManager() {
        mEventDao = new EventDao(App.getContext());
    }

    public List<Long> getFavoriteEventDays() {
        return mEventDao.selectDistrictFavoriteDateSafe();
    }

    public List<Long> getFavoriteEventsSafe() {
        return mEventDao.selectFavoriteEventsSafe();
    }

    public void setFavoriteEvent(long eventId, boolean isFavorite) {
        mEventDao.setFavoriteSafe(eventId, isFavorite);
    }
}
