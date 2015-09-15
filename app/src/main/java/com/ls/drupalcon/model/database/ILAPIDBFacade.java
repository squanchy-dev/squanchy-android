package com.ls.drupalcon.model.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;

public interface ILAPIDBFacade {


    void beginTransactions();

    void setTransactionSuccesfull();

    void endTransactions();

    ILAPIDBFacade open() throws SQLException;

    void close();

    boolean containsRecord(String theTable, String theWhereClause, String[] theColumns);


    boolean containsRecord(String theTable,
                           String theWhereClause,
                           String[] selectionArgs,
                           String[] theColumns);


    Cursor query(String theQuery, String[] selectionArgs);


    long save(String theTable, ContentValues theValues);

    /**
     * @param theTable
     * @param theWhereClause
     * @param theValues
     * @return returns number of rows affected
     */
    int update(String theTable, String theWhereClause, ContentValues theValues);


    int update(String theTable, String theWhereClause,
               String[] whereArgs,
               ContentValues theValues);


    int delete(String theTable, String theWhereClause,
               String[] whereArgs);


    int delete(String theTable, String theWhereClause);


    void insert(String sqlQuery);

    void execSQL(String sqlQuery, Object[] bindArgs);


    int clearTable(String theTable);


    Cursor getAllRecords(String theTable, String[] theColumns,
                         String theSelection);


    Cursor getAllRecords(String theTable, String[] theColumns,
                         String theSelection,
                         String[] selectionArgs);


    String getQuery(int theResId);
}
