package com.ls.drupalcon.model.managers;

import com.ls.drupal.AbstractBaseDrupalEntity;
import com.ls.drupal.DrupalClient;
import com.ls.drupalcon.model.dao.POIDao;
import com.ls.drupalcon.model.data.POI;
import com.ls.drupalcon.model.requests.PoisRequest;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PoisManager extends SynchronousItemManager<POI.Holder, Object, String> {

    private POIDao mPOIDao;

    public PoisManager(DrupalClient client) {
        super(client);
        mPOIDao = new POIDao();
    }

    @Override
    protected AbstractBaseDrupalEntity getEntityToFetch(DrupalClient client, Object requestParams) {
        return new PoisRequest(client);
    }

    @Override
    protected String getEntityRequestTag(Object params) {
        return "pois";
    }

    @Override
    protected boolean storeResponse(POI.Holder requestResponse, String tag) {
        List<POI> pois = requestResponse.getPOIs();
        if (pois == null) {
            return false;
        }

        mPOIDao.saveOrUpdateDataSafe(pois);
        for (POI poi : pois) {
            if (poi != null) {
                if (poi.isDeleted()) {
                    mPOIDao.saveOrUpdateDataSafe(pois);
                }
            }
        }
        return true;
    }

    public List<POI> getPOIs() {
        List<POI> pois = mPOIDao.getAllSafe();
        Collections.sort(pois, new Comparator<POI>() {
            @Override
            public int compare(POI poi, POI poi2) {
                return Double.compare(poi.getOrder(), poi2.getOrder());
            }
        });
        return pois;
    }

    public void clear() {
        mPOIDao.clearData();
    }
}

