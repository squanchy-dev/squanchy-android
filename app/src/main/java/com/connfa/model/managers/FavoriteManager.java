package com.connfa.model.managers;

import android.content.Context;

import com.connfa.model.dao.EventDao;

import java.util.List;

public class FavoriteManager {

    private EventDao mEventDao;

    public FavoriteManager(Context context) {
        mEventDao = new EventDao(context);
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
