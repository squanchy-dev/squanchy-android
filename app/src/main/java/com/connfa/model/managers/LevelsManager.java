package com.connfa.model.managers;

import android.content.Context;

import com.connfa.model.dao.LevelDao;
import com.connfa.model.data.Level;
import com.connfa.model.requests.LevelsRequest;
import com.ls.drupal.AbstractBaseDrupalEntity;
import com.ls.drupal.DrupalClient;

import java.util.List;

public class LevelsManager extends SynchronousItemManager<Level.Holder, Object, String> {

    private LevelDao mLevelDao;

    public LevelsManager(Context context, DrupalClient client) {
        super(context, client);
        mLevelDao = new LevelDao(context);
    }

    @Override
    protected AbstractBaseDrupalEntity getEntityToFetch(DrupalClient client, Object requestParams) {
        return new LevelsRequest(getContext(), client);
    }

    @Override
    protected String getEntityRequestTag(Object params) {
        return "levels";
    }

    @Override
    protected boolean storeResponse(Level.Holder requestResponse, String tag) {
        List<Level> levels = requestResponse.getLevels();
        if (levels == null) {
            return false;
        }

        mLevelDao.saveOrUpdateDataSafe(levels);
        for (Level level : levels) {
            if (level != null) {
                if (level.isDeleted()) {
                    deleteLevel(level);
                }
            }
        }
        return true;
    }

    public Level getLevel(long levelId) {
        List<Level> data = mLevelDao.getDataSafe(levelId);
        return data.size() > 0 ? data.get(0) : null;
    }

    public List<Level> getLevels() {
        return mLevelDao.getAllSafe();
    }

    public void deleteLevel(Level level) {
        mLevelDao.deleteDataSafe(level.getId());
    }

    public void clear() {
        mLevelDao.deleteAll();
    }
}
