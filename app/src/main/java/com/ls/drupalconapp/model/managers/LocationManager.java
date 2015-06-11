package com.ls.drupalconapp.model.managers;

import com.ls.drupal.AbstractBaseDrupalEntity;
import com.ls.drupal.DrupalClient;
import com.ls.drupalconapp.model.DatabaseManager;
import com.ls.drupalconapp.model.data.Location;
import com.ls.drupalconapp.model.requests.LocationRequest;

import java.util.List;

public class LocationManager extends SynchronousItemManager<Location.Holder, Object, String> {

    public LocationManager(DrupalClient client) {
        super(client);
    }

    @Override
    protected AbstractBaseDrupalEntity getEntityToFetch(DrupalClient client, Object requestParams) {
        return new LocationRequest(client);
    }

    @Override
    protected String getEntityRequestTag(Object params) {
        return "location";
    }

    @Override
    protected boolean storeResponse(Location.Holder requestResponse, String tag) {
        DatabaseManager databaseManager = DatabaseManager.instance();
        List<Location> locations = requestResponse.getLocations();

        if (locations == null) {
            return false;
        }

        databaseManager.saveLocations(locations);

        for (Location location : locations) {
            if (location != null) {
                if (location.isDeleted()) {
                    databaseManager.deleteLocation(location);
                }
            }
        }
        return true;
    }
}
