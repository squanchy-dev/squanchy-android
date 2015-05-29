package com.ls.drupalconapp.model.dao;

import android.content.Context;

import com.ls.drupalconapp.model.AppDatabaseInfo;
import com.ls.drupalconapp.model.data.InfoItem;
import com.ls.drupalconapp.model.data.Type;
import com.ls.drupalconapp.model.database.AbstractEntityDAO;


/**
 * Created by Yakiv M. on 25.04.14.
 */

public class InfoDao extends AbstractEntityDAO<InfoItem, Long> {

    public static final String TABLE_NAME = "table_info";
    private final Context mContext;

    public InfoDao(Context context) {
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
    protected InfoItem newInstance() {
        return new InfoItem();
    }

    @Override
    protected String[] getKeyColumns() {
        return new String[0];
    }
}
