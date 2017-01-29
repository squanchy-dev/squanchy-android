package com.connfa.model.managers;

import android.content.Context;

import com.connfa.model.dao.POIDao;
import com.connfa.model.data.POI;
import com.connfa.service.api.ConnfaRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;

public class PoisManager extends SynchronousItemManager<POI.Holder, String> {

    private POIDao mPOIDao;

    public PoisManager(Context context) {
        super(context);
        mPOIDao = new POIDao(context);
    }

    @Override
    protected String getEntityRequestTag() {
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

    @Override
    protected Observable<POI.Holder> doFetch(ConnfaRepository repository) {
        return repository.pois();
    }

    public List<POI> getPOIs() {
        List<POI> pois = mPOIDao.getAllSafe();
        Collections.sort(pois, (poi, poi2) -> Double.compare(poi.getOrder(), poi2.getOrder()));
        return pois;
    }

    public void clear() {
        mPOIDao.clearData();
    }
}

