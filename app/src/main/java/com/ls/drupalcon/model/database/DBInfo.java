package com.ls.drupalcon.model.database;

import java.util.List;

public interface DBInfo {
    /**
     * Method returns database name.
     *
     * @return database name
     */
    String getDatabaseName();

    /**
     * Method returns database version
     *
     * @return
     */
    int getDatabaseVersion();

    /**
     * Method returns list of queries which create database schema.
     *
     * @return list of queries
     */
    List<String> getTableCreationQueries();

    /**
     * Method returns list table names in this database .
     *
     * @return list of names
     */
    List<String> getTableNameList();

    /**
     * @return Migration task for current database
     */
    IMigrationTask getMigrationTask();
}
