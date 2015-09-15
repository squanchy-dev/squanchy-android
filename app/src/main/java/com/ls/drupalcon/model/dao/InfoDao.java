package com.ls.drupalcon.model.dao;

import com.ls.drupalcon.model.AppDatabaseInfo;
import com.ls.drupalcon.model.data.InfoItem;
import com.ls.drupalcon.model.database.AbstractEntityDAO;


public class InfoDao extends AbstractEntityDAO<InfoItem, Long> {

    public static final String TABLE_NAME = "table_info";

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
