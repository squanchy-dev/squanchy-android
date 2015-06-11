package com.ls.drupalconapp.model.managers;

import com.ls.drupal.AbstractBaseDrupalEntity;
import com.ls.drupal.DrupalClient;
import com.ls.drupalconapp.model.DatabaseManager;
import com.ls.drupalconapp.model.data.POI;
import com.ls.drupalconapp.model.requests.PoisRequest;

import java.util.List;

public class PoisManager extends SynchronousItemManager<POI.Holder,Object,String>{

    public PoisManager(DrupalClient client) {
        super(client);
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
        DatabaseManager databaseManager = DatabaseManager.instance();
        List<POI> pois = requestResponse.getPOIs();

        if (pois == null) {
            return false;
        }

        databaseManager.savePOIs(pois);

        for (POI poi : pois){
            if(poi != null) {
                if (poi.isDeleted()) {
                    databaseManager.deletePOI(poi);
                }
            }
        }
        return true;
    }
}

