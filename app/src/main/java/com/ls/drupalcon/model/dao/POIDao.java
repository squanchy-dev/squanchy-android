package com.ls.drupalcon.model.dao;

import com.ls.drupalcon.model.AppDatabaseInfo;
import com.ls.drupalcon.model.data.POI;
import com.ls.drupalcon.model.database.AbstractEntityDAO;

public class POIDao extends AbstractEntityDAO<POI, Long> {

    public static final String TABLE_NAME = "table_poi";

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
    protected POI newInstance() {
        return new POI();
    }

    @Override
    protected String[] getKeyColumns() {
        return new String[0];
    }
}
