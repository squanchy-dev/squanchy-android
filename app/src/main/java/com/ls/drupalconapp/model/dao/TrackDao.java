package com.ls.drupalconapp.model.dao;

import com.ls.drupalconapp.model.AppDatabaseInfo;
import com.ls.drupalconapp.model.data.Track;
import com.ls.drupalconapp.model.database.AbstractEntityDAO;

import android.content.Context;


/**
 * Created by Yakiv M. on 25.04.14.
 */

public class TrackDao extends AbstractEntityDAO<Track, Long> {

    public static final String TABLE_NAME = "table_track";
    private final Context mContext;

    public TrackDao(Context context) {
        mContext = context;
    }

    @Override
    protected String getSearchCondition() {
        return "_id=?";
    }

    @Override
    protected String[] getSearchConditionArguments(Long theId) {
        return new String[]{theId.toString()};
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getDatabaseName() {
        return AppDatabaseInfo.DATABASE_NAME;
    }

    @Override
    protected Track newInstance() {
        return new Track();
    }

    @Override
    protected String[] getKeyColumns() {
        return new String[0];
    }
}
