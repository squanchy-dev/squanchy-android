package com.ls.drupalcon.model.managers;

import com.ls.drupal.AbstractBaseDrupalEntity;
import com.ls.drupal.DrupalClient;
import com.ls.drupal.DrupalImageEntity;
import com.ls.drupalcon.app.App;
import com.ls.drupalcon.model.dao.FloorPlanDao;
import com.ls.drupalcon.model.dao.LocationDao;
import com.ls.drupalcon.model.data.FloorPlan;
import com.ls.drupalcon.model.data.Location;
import com.ls.drupalcon.model.requests.FloorPlansRequest;
import com.ls.drupalcon.model.requests.LocationRequest;
import com.ls.utils.FileUtils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.util.Collections;
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
        for (FloorPlan floor : locations) {
            if (floor != null) {
                if (!floor.isDeleted()){

                    //Load new image
                    DrupalImageEntity imageEntity = new DrupalImageEntity(getClient());
                    imageEntity.setImagePath(floor.getImageURL());
                    imageEntity.pullFromServer(true,floor.getImageURL(),null);
                    Drawable imageDrawable = imageEntity.getManagedData();

                    //Store image
                    if(imageDrawable instanceof BitmapDrawable){
                        Bitmap image = ((BitmapDrawable)imageDrawable).getBitmap();
                        if(!FileUtils.writeBitmapToStorage(floor.getFilePath(),image,App.getContext())){
                            return false;
                        }
                    }else{
                        return false;
                    }

                }
            }
        }

        for (FloorPlan floor : locations) {
            if (floor != null) {
                if (floor.isDeleted()) {
                    if(mFloorPlansDAO.deleteDataSafe(floor.getId()) > 0) {
                        FileUtils.deleteStoredFile(floor.getFilePath(), App.getContext());
                    }
                }
            }
        }

        return true;
    }

    public List<FloorPlan> getFloorPlans() {
        List<FloorPlan> result = mFloorPlansDAO.getAllSafe();
        Collections.sort(result);
        return result;
    }

    public Bitmap getImageForPlan(FloorPlan plan){
        Bitmap planImage = FileUtils.readBitmapFromStoredFile(plan.getFilePath(),App.getContext());
        return planImage;
    }

    public void clear() {
        mFloorPlansDAO.deleteAll();
    }
}
