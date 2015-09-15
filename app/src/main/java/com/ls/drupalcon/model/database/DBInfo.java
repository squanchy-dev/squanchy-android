package com.ls.drupalcon.model.database;

import java.util.List;

public interface DBInfo
{
	/**
	 * Method returns database name.
	 *  
	 * @return database name
	 */
	public String getDatabaseName();
	/**
	 * Method returns database version
	 * @return
	 */
	public int getDatabaseVersion();
	/**
	 * Method returns list of queries which create database schema.
	 * 
	 * @return list of queries
	 */
	public List<String> getTableCreationQueries();
	   /**
     * Method returns list table names in this database .
     * 
     * @return list of names
     */
	public List<String> getTableNameList();
	
	/**	 
	 * @return Migration task for current database
	 */
	public IMigrationTask getMigrationTask();
}
