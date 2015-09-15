package com.ls.drupalcon.model.database;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class LAPIDBRegister {

    private static LAPIDBRegister instance;

    public synchronized static LAPIDBRegister getInstance() {
        if (instance == null) {
            instance = new LAPIDBRegister();
        }

        return instance;
    }


    private Map<String, ILAPIDBFacade> map;


    private LAPIDBRegister() {
        this.map = new HashMap<String, ILAPIDBFacade>();
    }


    public void register(Context theContext, DBInfo theDbInfo) {
        if (theDbInfo == null) {
            throw new IllegalArgumentException("DBInfo can't be null");
        }

        String databaseName = theDbInfo.getDatabaseName();
        if (this.map.containsKey(databaseName)) {
            return;
        }

        this.map.put(databaseName, new DatabaseFacade(theContext, theDbInfo));
    }


    public void unregister(String theDatabaseName) {
        this.map.remove(theDatabaseName);
    }


    public ILAPIDBFacade lookup(String theDatabaseName) {
        if (theDatabaseName == null) {
            throw new IllegalArgumentException("Database name can't be null");
        }

        return this.map.get(theDatabaseName);
    }


}
