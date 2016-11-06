package com.connfa.model.managers;

import android.content.Context;
import android.graphics.Bitmap;

import com.ls.drupal.AbstractBaseDrupalEntity;
import com.ls.drupal.DrupalByteEntity;
import com.ls.drupal.DrupalClient;
import com.connfa.model.dao.FloorPlanDao;
import com.connfa.model.data.FloorPlan;
import com.connfa.model.requests.FloorPlansRequest;
import com.ls.http.base.BaseRequest;
import com.ls.http.base.ResponseData;
import com.ls.util.L;
import com.connfa.utils.FileUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class FloorPlansManager extends SynchronousItemManager<FloorPlan.Holder, Object, String> {

    private final Context mContext;

    private FloorPlanDao mFloorPlansDAO;

    public FloorPlansManager(DrupalClient client, Context context) {
        super(client);
        this.mFloorPlansDAO = new FloorPlanDao();
        this.mContext = context;
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
        List<FloorPlan> plans = requestResponse.getFloorPlans();
        if (plans == null) {
            return false;
        }

        L.e("Plans loaded:" + plans);
        mFloorPlansDAO.saveOrUpdateDataSafe(plans);
        for (FloorPlan floor : plans) {
            if (floor != null) {
                if (!floor.isDeleted()) {
                    if (!loadImageForFloor(floor)) {
                        L.e("Image loading failed:" + floor.getImageURL());
                        return false;
                    }
                }
            }
        }

        for (FloorPlan floor : plans) {
            if (floor != null) {
                if (floor.isDeleted()) {
                    if (mFloorPlansDAO.deleteDataSafe(floor.getId()) > 0) {
                        FileUtils.deleteStoredFile(floor.getFilePath(), mContext);
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

    public Bitmap getImageForPlan(FloorPlan plan, int requiredWidth, int requiredHeight) {
        Bitmap planImage = FileUtils.readBitmapFromStoredFile(plan.getFilePath(), requiredWidth, requiredHeight, mContext);
        return planImage;
    }

    public void clear() {
        mFloorPlansDAO.deleteAll();
    }

//    private boolean loadImageForFloor(FloorPlan floor){
//        //Load new image
//        DrupalImageEntity imageEntity = new DrupalImageEntity(getClient());
//        imageEntity.setImagePath(floor.getImageURL());
//        try {
//            imageEntity.pullFromServer(true, floor.getImageURL(), null);
//            Drawable imageDrawable = imageEntity.getManagedData();
//
//            //Store image
//            if (imageDrawable instanceof BitmapDrawable) {
//                Bitmap image = ((BitmapDrawable) imageDrawable).getBitmap();
//                return FileUtils.writeBitmapToStorage(floor.getFilePath(), image, App.getContext());
//            } else {
//                return false;
//            }
//        }catch (Error e){
//            return false;
//        }
//    }

    private boolean loadImageForFloor(final FloorPlan floor) {
        //Load new image
        DrupalByteEntity imageEntity = new DrupalByteEntity(getClient()) {
            @Override
            protected String getPath() {
                return floor.getImageURL();
            }

            @Override
            protected Map<String, String> getItemRequestPostParameters() {
                return null;
            }

            @Override
            protected Map<String, Object> getItemRequestGetParameters(BaseRequest.RequestMethod method) {
                return null;
            }
        };

        final CountDownLatch latch = new CountDownLatch(1);
        final ResponseResult result = new ResponseResult();

        try {
            imageEntity.pullFromServer(true, floor.getImageURL(), new AbstractBaseDrupalEntity.OnEntityRequestListener() {
                @Override
                public void onRequestCompleted(AbstractBaseDrupalEntity entity, Object tag, ResponseData data) {
                    byte[] imageData = (byte[]) data.getData();

                    //Store image
                    if (imageData != null && imageData.length > 0) {
                        result.isSuccessful = FileUtils.writeBytesToStorage(floor.getFilePath(), imageData, mContext);
                    } else {
                        result.isSuccessful = false;
                    }
                    latch.countDown();
                }

                @Override
                public void onRequestFailed(AbstractBaseDrupalEntity entity, Object tag, ResponseData data) {
                    result.isSuccessful = false;
                    latch.countDown();
                }

                @Override
                public void onRequestCanceled(AbstractBaseDrupalEntity entity, Object tag) {
                    result.isSuccessful = false;
                    latch.countDown();
                }
            });

            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result.isSuccessful;
    }

    private class ResponseResult {
        public boolean isSuccessful;
    }
}
