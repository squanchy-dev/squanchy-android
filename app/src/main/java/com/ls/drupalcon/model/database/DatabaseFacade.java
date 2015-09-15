package com.ls.drupalcon.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseFacade
        implements ILAPIDBFacade {
//
//	private static DatabaseFacade instance;

    private DBInfo dbInfo;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    private final Context context;
    private int openCounter = 0;

	/*
    public static DatabaseFacade instance(Context theContext, DBInfo theDbInfo)
	{
		if (instance == null)
		{
			instance = new DatabaseFacade(theContext, theDbInfo);
		}

		return instance;
	}

	
	public static DatabaseFacade instance()
	{
		if (instance == null)
		{
			throw new IllegalStateException("Called method on uninitialized database facade");
		}

		return instance;
	}*/


    public DatabaseFacade(Context theContext, DBInfo theDbInfo) {
        this.context = theContext;
        this.dbInfo = theDbInfo;
    }


    public void beginTransactions() {
        db.beginTransaction();
    }


    public void setTransactionSuccesfull() {
        db.setTransactionSuccessful();
    }


    public void endTransactions() {
        db.endTransaction();
    }


    public synchronized DatabaseFacade open()
            throws SQLException {
        if (this.openCounter == 0) {
            this.dbHelper = new DatabaseHelper(this.context, this.dbInfo);
            this.db = this.dbHelper.getWritableDatabase();
        }

        this.openCounter++;

        return this;
    }


    public synchronized void close() {
        if (openCounter == 1) {
            this.db.close();
            this.dbHelper.close();
        }

        openCounter--;
    }


    public boolean containsRecord(String theTable, String theWhereClause, String[] theColumns) {
        boolean result = false;

        Cursor cursor = db.query(true,
                theTable,
                theColumns,
                theWhereClause,// selection
                null,// selection args
                null,// groupBy
                null,// having
                null,// order by
                null);// limit

        result = cursor.getCount() > 0;

        cursor.close();
//        cursor = null;

        return result;
    }


    public boolean containsRecord(String theTable, String theWhereClause, String[] selectionArgs,
                                  String[] theColumns) {
        boolean result = false;

        Cursor cursor = db.query(true,
                theTable,
                theColumns,
                theWhereClause,// selection
                selectionArgs,// selection args
                null,// groupBy
                null,// having
                null,// order by
                null);// limit

        result = cursor.getCount() > 0;

        cursor.close();
//        cursor = null;

        return result;
    }


    public Cursor query(String theQuery, String[] selectionArgs) {
        return db.rawQuery(theQuery, selectionArgs);
    }


    public long save(String theTable, ContentValues theValues) {
        return db.insert(theTable, null, theValues);
    }

    /**
     * @param theTable
     * @param theWhereClause
     * @param theValues
     * @return returns number of rows affected
     */
    public int update(String theTable, String theWhereClause, ContentValues theValues) {
        return db.update(theTable, theValues, theWhereClause, null);
    }


    public int update(String theTable, String theWhereClause, String[] whereArgs,
                      ContentValues theValues) {
        return db.update(theTable, theValues, theWhereClause, whereArgs);
    }


    public int delete(String theTable, String theWhereClause, String[] whereArgs) {
        return db.delete(theTable, theWhereClause, whereArgs);
    }


    public int delete(String theTable, String theWhereClause) {
        return db.delete(theTable, theWhereClause, null);
    }


    public void insert(String sqlQuery) {
        db.execSQL(sqlQuery);
    }

    public void execSQL(String sqlQuery, Object[] bindArgs) {
        db.execSQL(sqlQuery, bindArgs);
    }


    public int clearTable(String theTable) {
        return this.delete(theTable, null, null);
    }


    public Cursor getAllRecords(String theTable, String[] theColumns, String theSelection) {
        Cursor cursor = db.query(true,
                theTable,
                theColumns,
                theSelection,// selection
                null,// selection args
                null,// groupBy
                null,// having
                null,// order by
                null);// limit

        return cursor;
    }


    public Cursor getAllRecords(String theTable, String[] theColumns, String theSelection,
                                String[] selectionArgs) {
        Cursor cursor = db.query(true,
                theTable,
                theColumns,
                theSelection,// selection
                selectionArgs,// selection args
                null,// groupBy
                null,// having
                null,// order by
                null);// limit

        return cursor;
    }


    public String getQuery(int theResId) {
        return context.getString(theResId);
    }

}
