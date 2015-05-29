package com.ls.drupalconapp.model.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;

/**
 * 
 * @author Lemberg
 * @version 3.00
 * 
 */

public interface ILAPIDBFacade
{

		
	public void beginTransactions();
	
	public void setTransactionSuccesfull();
	
	public void endTransactions();
	
	public ILAPIDBFacade open() throws SQLException;
	
	public void close();
    
	public boolean containsRecord(String theTable, String theWhereClause, String[] theColumns);
    
	
	public boolean containsRecord(String theTable,
            String theWhereClause,
            String[] selectionArgs,
            String[] theColumns);
	
	
	public Cursor query(String theQuery, String[] selectionArgs);
	
    
    public long save(String theTable, ContentValues theValues);
    
    /**
     * 
     * @param theTable
     * @param theWhereClause
     * @param theValues
     * @return returns number of rows affected
     */
    public int update(String theTable, String theWhereClause, ContentValues theValues);
    
    
    public int update(String theTable, String theWhereClause,
            String[] whereArgs,
            ContentValues theValues);
    
    
    public int delete(String theTable, String theWhereClause,
            String[] whereArgs);
    
    
    public int delete(String theTable, String theWhereClause);
    
    
    public void insert(String sqlQuery);

    public void execSQL(String sqlQuery, Object[] bindArgs);
    
    
    public int clearTable(String theTable);
    
    
    public Cursor getAllRecords(String theTable, String[] theColumns,
            String theSelection);
    
    
    public Cursor getAllRecords(String theTable, String[] theColumns,
            String theSelection,
            String[] selectionArgs);
    
    
    public String getQuery(int theResId);
}
