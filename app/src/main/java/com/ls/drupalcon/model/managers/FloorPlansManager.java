package com.ls.drupalcon.model.managers;

import com.ls.drupal.AbstractBaseDrupalEntity;
import com.ls.drupal.DrupalClient;
import com.ls.drupalcon.model.dao.FloorPlanDao;
import com.ls.drupalcon.model.dao.LocationDao;
import com.ls.drupalcon.model.data.FloorPlan;
import com.ls.drupalcon.model.data.Location;
import com.ls.drupalcon.model.requests.FloorPlansRequest;
import com.ls.drupalcon.model.requests.LocationRequest;

import java.util.List;

public class FloorPlansManager extends SynchronousItemManager<FloorPlan.Holder, Object, String> {

    private FloorPlanDao mFloorPlansDAO;
    public FloorPlansManager(DrupalClient client) {
        super(client);
        mFloorPlansDAO = new FloorPlanDao();
    }

    @Override
    protected AbstractBaseDrupalEntity getEntityToFetch(DrupalClient client, Object requestParams) {
        return new FloorPlansRequest(client);
    }

    @Override
    protected String getEntityRequestTag(Object params) {
        return "floorPlans";
    }

    @Override
    protected boolean storeResponse(FloorPlan.Holder requestResponse, String tag) {
        List<FloorPlan> locations = requestResponse.getLocations();
        if (locations == null) {
            return false;
        }

        mFloorPlansDAO.saveOrUpdateDataSafe(locations);
        for (FloorPlan location : locations) {
            if (location != null) {
                if (location.isDeleted()) {
                    mFloorPlansDAO.deleteDataSafe(location.getId());
                    //TODO: add image removal
                }
            }
        }
        return true;
    }

    public List<FloorPlan> getFloorPlans() {
        return mFloorPlansDAO.getAllSafe();
    }

    public void clear() {
        mFloorPlansDAO.deleteAll();
    }
}
