package com.ls.drupalconapp.model.dao;

import com.ls.drupalconapp.model.AppDatabaseInfo;
import com.ls.drupalconapp.model.data.Type;
import com.ls.drupalconapp.model.database.AbstractEntityDAO;

import android.content.Context;


/**
 * Created by Yakiv M. on 25.04.14.
 */

public class TypeDao extends AbstractEntityDAO<Type, Long> {

    public static final String TABLE_NAME = "table_type";
    private final Context mContext;

    public TypeDao(Context context) {
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
    protected Type newInstance() {
        return new Type();
    }

    @Override
    protected String[] getKeyColumns() {
        return new String[0];
    }
}
