package com.ls.drupalcon.model.dao;

import com.ls.drupalcon.model.AppDatabaseInfo;
import com.ls.drupalcon.model.data.Type;
import com.ls.drupalcon.model.database.AbstractEntityDAO;

public class TypeDao extends AbstractEntityDAO<Type, Long> {

    public static final String TABLE_NAME = "table_type";

    public TypeDao() {
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
