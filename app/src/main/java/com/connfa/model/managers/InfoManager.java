package com.connfa.model.managers;

import android.content.Context;

import com.connfa.model.PreferencesManager;
import com.connfa.model.dao.InfoDao;
import com.connfa.model.data.InfoItem;
import com.connfa.service.api.ConnfaRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;

public class InfoManager extends SynchronousItemManager<InfoItem.General, String> {

    private InfoDao infoDAO;
    private final PreferencesManager preferencesManager;

    public InfoManager(Context context) {
        super(context);
        infoDAO = new InfoDao(context);
        preferencesManager = PreferencesManager.create(context);
    }

    @Override
    protected String getEntityRequestTag() {
        return "info";
    }

    @Override
    protected boolean storeResponse(InfoItem.General requestResponse, String tag) {
        List<InfoItem> infoList = requestResponse.getInfo();
        if (infoList == null) {
            return false;
        }

        infoDAO.saveOrUpdateDataSafe(infoList);
        for (InfoItem info : infoList) {
            if (info != null) {
                if (info.isDeleted()) {
                    infoDAO.deleteDataSafe(info.getId());
                }
            }
        }
        preferencesManager.saveMajorInfoTitle(requestResponse.getMajorTitle());
        preferencesManager.saveMinorInfoTitle(requestResponse.getMinorTitle());

        return true;
    }

    @Override
    protected Observable<InfoItem.General> doFetch(ConnfaRepository repository) {
        return repository.info();
    }

    public List<InfoItem> getInfo() {
        List<InfoItem> infoItems = infoDAO.getAllSafe();
        Collections.sort(infoItems, new Comparator<InfoItem>() {
            @Override
            public int compare(InfoItem infoItem, InfoItem infoItem2) {
                return Double.compare(infoItem.getOrder(), infoItem2.getOrder());
            }
        });

        return infoItems;
    }

    public void clear() {
        infoDAO.deleteAll();
    }
}
