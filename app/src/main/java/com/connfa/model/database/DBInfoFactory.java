package com.connfa.model.database;

import android.content.Context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DBInfoFactory {

    public static DBInfo create(final Context context, final List<String> tableNames,
                                final List<Integer> resIdList, final int databaseVersion, final String databaseName) {

        return new DBInfo() {

            @Override
            public List<String> getTableCreationQueries() {
                final List<String> queryList = new ArrayList<>();

                for (Integer resId : resIdList) {
                    queryList.add(context.getString(resId));
                }

                return queryList;
            }

            @Override
            public List<String> getTableNameList() {
                if (tableNames == null) {
                    throw new IllegalArgumentException("The table name list cannot be null");
                }

                return tableNames;
            }

            @Override
            public int getDatabaseVersion() {
                return databaseVersion;
            }

            @Override
            public String getDatabaseName() {
                return databaseName;
            }

            @Override
            public IMigrationTask getMigrationTask() {
                return (theDb, oldVersion, newVersion) -> {
                    for (String tableName : tableNames) {
                        theDb.execSQL("DROP TABLE IF EXISTS " + tableName);
                    }
                };
            }
        };
    }
}
