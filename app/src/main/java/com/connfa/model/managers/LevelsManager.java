package com.connfa.model.managers;

import android.content.Context;

import com.connfa.model.dao.LevelDao;
import com.connfa.model.data.Level;
import com.connfa.service.ConnfaRepository;

import java.util.List;

import io.reactivex.Observable;

public class LevelsManager extends SynchronousItemManager<Level.Holder, String> {

    private LevelDao mLevelDao;

    public LevelsManager(Context context) {
        super(context);
        mLevelDao = new LevelDao(context);
    }

    @Override
    protected String getEntityRequestTag() {
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

    @Override
    protected Observable<Level.Holder> doFetch(ConnfaRepository repository) {
        return repository.levels();
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
