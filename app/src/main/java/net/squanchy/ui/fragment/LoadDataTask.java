package net.squanchy.ui.fragment;

import android.os.AsyncTask;
import android.support.annotation.Nullable;

import net.squanchy.model.Model;
import net.squanchy.model.managers.BofsManager;
import net.squanchy.model.managers.FavoriteManager;
import net.squanchy.model.managers.ProgramManager;
import net.squanchy.model.managers.SocialManager;
import net.squanchy.ui.drawer.DrawerManager;

import java.util.ArrayList;
import java.util.List;

class LoadDataTask extends AsyncTask<Void, Void, List<Long>> {

    private final DrawerManager.EventMode eventMode;
    private final LoadDataTaskCallback callback;

    LoadDataTask(DrawerManager.EventMode eventMode, LoadDataTaskCallback callback) {
        this.eventMode = eventMode;
        this.callback = callback;
    }

    @Override
    @Nullable
    protected List<Long> doInBackground(Void... params) {
        if (isCancelled()) {
            return null;
        }

        List<Long> dayList = new ArrayList<>();
        switch (eventMode) {
            case BOFS:
                BofsManager bofsManager = Model.getInstance().getBofsManager();
                dayList.addAll(bofsManager.getBofsDays());
                break;
            case SOCIAL:
                SocialManager socialManager = Model.getInstance().getSocialManager();
                dayList.addAll(socialManager.getSocialsDays());
                break;
            case FAVORITES:
                FavoriteManager favoriteManager = Model.getInstance().getFavoriteManager();
                dayList.addAll(favoriteManager.getFavoriteEventDays());
                break;
            default:
                ProgramManager programManager = Model.getInstance().getProgramManager();
                dayList.addAll(programManager.getProgramDays());
                break;
        }

        if (isCancelled()) {
            return null;
        }

        return dayList;
    }

    @Override
    protected void onPostExecute(@Nullable List<Long> result) {
        if (result != null && callback != null) {
            callback.onDataLoaded(result);
        }
    }

    interface LoadDataTaskCallback {

        void onDataLoaded(List<Long> result);
    }
}
