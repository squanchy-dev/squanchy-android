package com.ls.drupalcon.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DBInfoFactory {

    public static DBInfo create(final Context theContext, final List<String> theTableNameList,
                                final List<Integer> theResIdList, final int theDatabaseVersion, final String theDatabaseName) {
        DBInfo result = new DBInfo() {

            @Override
            public List<String> getTableCreationQueries() {
                final List<String> queryList = new ArrayList<String>();

                for (Integer resId : theResIdList) {
                    queryList.add(theContext.getString(resId));
                }

                return queryList;
            }

            @Override
            public List<String> getTableNameList() {
                if (theTableNameList == null) {
                    throw new IllegalArgumentException("The table name list cannot be null");
                }

                return theTableNameList;
            }

            @Override
            public int getDatabaseVersion() {
                return theDatabaseVersion;
            }

            @Override
            public String getDatabaseName() {
                return theDatabaseName;
            }

            @Override
            public IMigrationTask getMigrationTask() {
                return new IMigrationTask() {

                    @Override
                    public void onUpgrade(SQLiteDatabase theDb, int oldVersion, int newVersion) {
                        Iterator<String> it = theTableNameList.iterator();
                        while (it.hasNext()) {
                            String tableName = it.next();
                            theDb.execSQL("DROP TABLE IF EXISTS " + tableName);
                        }
                    }
                };
            }
        };

        return result;
    }

}
