package com.ls.drupalcon.model.managers;

import com.ls.drupal.AbstractBaseDrupalEntity;
import com.ls.drupal.DrupalClient;
import com.ls.drupalcon.model.PreferencesManager;
import com.ls.drupalcon.model.dao.InfoDao;
import com.ls.drupalcon.model.data.InfoItem;
import com.ls.drupalcon.model.requests.InfoRequest;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class InfoManager extends SynchronousItemManager<InfoItem.General, Object, String> {

    private InfoDao mInfoDao;

    public InfoManager(DrupalClient client) {
        super(client);
        mInfoDao = new InfoDao();
    }

    @Override
    protected AbstractBaseDrupalEntity getEntityToFetch(DrupalClient client, Object requestParams) {
        return new InfoRequest(client);
    }

    @Override
    protected String getEntityRequestTag(Object params) {
        return "info";
    }

    @Override
    protected boolean storeResponse(InfoItem.General requestResponse, String tag) {
        List<InfoItem> infoList = requestResponse.getInfo();
        if (infoList == null) {
            return false;
        }


        mInfoDao.saveOrUpdateDataSafe(infoList);
        for (InfoItem info : infoList) {
            if (info != null) {
                if (info.isDeleted()) {
                    mInfoDao.deleteDataSafe(info.getId());
                }
            }
        }
        PreferencesManager.getInstance().saveMajorInfoTitle(requestResponse.getMajorTitle());
        PreferencesManager.getInstance().saveMinorInfoTitle(requestResponse.getMinorTitle());

        return true;
    }

    public List<InfoItem> getInfo() {
        List<InfoItem> infoItems = mInfoDao.getAllSafe();
        Collections.sort(infoItems, new Comparator<InfoItem>() {
            @Override
            public int compare(InfoItem infoItem, InfoItem infoItem2) {
                return Double.compare(infoItem.getOrder(), infoItem2.getOrder());
            }
        });

        return infoItems;
    }

    public void clear() {
        mInfoDao.deleteAll();
    }
}