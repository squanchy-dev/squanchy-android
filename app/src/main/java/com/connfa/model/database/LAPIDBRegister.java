package com.connfa.model.database;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class LAPIDBRegister {

    private static LAPIDBRegister instance;

    private Map<String, ILAPIDBFacade> map;

    public synchronized static LAPIDBRegister getInstance() {
        if (instance == null) {
            instance = new LAPIDBRegister();
        }

        return instance;
    }

    private LAPIDBRegister() {
        this.map = new HashMap<>();
    }

    public void register(Context context, DBInfo dbInfo) {
        if (dbInfo == null) {
            throw new IllegalArgumentException("DBInfo can't be null");
        }

        String databaseName = dbInfo.getDatabaseName();
        if (map.containsKey(databaseName)) {
            return;
        }

        map.put(databaseName, new DatabaseFacade(context, dbInfo));
    }

    public void unregister(String databaseName) {
        map.remove(databaseName);
    }

    public ILAPIDBFacade lookup(String databaseName) {
        if (databaseName == null) {
            throw new IllegalArgumentException("Database name can't be null");
        }

        return map.get(databaseName);
    }

}
