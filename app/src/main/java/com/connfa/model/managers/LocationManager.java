package com.connfa.model.managers;

import android.content.Context;

import com.connfa.model.dao.LocationDao;
import com.connfa.model.data.Location;
import com.connfa.model.requests.LocationRequest;
import com.ls.drupal.AbstractBaseDrupalEntity;
import com.ls.drupal.DrupalClient;

import java.util.List;

public class LocationManager extends SynchronousItemManager<Location.Holder, Object, String> {

    private LocationDao mLocationDao;

    public LocationManager(Context context, DrupalClient client) {
        super(context, client);
        mLocationDao = new LocationDao(context);
    }

    @Override
    protected AbstractBaseDrupalEntity getEntityToFetch(DrupalClient client, Object requestParams) {
        return new LocationRequest(getContext(), client);
    }

    @Override
    protected String getEntityRequestTag(Object params) {
        return "location";
    }

    @Override
    protected boolean storeResponse(Location.Holder requestResponse, String tag) {
        List<Location> locations = requestResponse.getLocations();
        if (locations == null) {
            return false;
        }

        mLocationDao.saveOrUpdateDataSafe(locations);
        for (Location location : locations) {
            if (location != null) {
                if (location.isDeleted()) {
                    mLocationDao.deleteDataSafe(location.getId());
                }
            }
        }
        return true;
    }

    public List<Location> getLocations() {
        return mLocationDao.getAllSafe();
    }

    public void clear() {
        mLocationDao.deleteAll();
    }
}
