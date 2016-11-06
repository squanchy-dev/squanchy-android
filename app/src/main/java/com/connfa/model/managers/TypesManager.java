package com.connfa.model.managers;

import com.connfa.model.dao.TypeDao;
import com.connfa.model.data.Type;
import com.connfa.model.requests.TypesRequest;
import com.ls.drupal.AbstractBaseDrupalEntity;
import com.ls.drupal.DrupalClient;

import java.util.List;

public class TypesManager extends SynchronousItemManager<Type.Holder, Object, String> {

    private TypeDao mTypeDao;

    public TypesManager(DrupalClient client) {
        super(client);
        mTypeDao = new TypeDao();
    }

    @Override
    protected AbstractBaseDrupalEntity getEntityToFetch(DrupalClient client, Object requestParams) {
        return new TypesRequest(client);
    }

    @Override
    protected String getEntityRequestTag(Object params) {
        return "types";
    }

    @Override
    protected boolean storeResponse(Type.Holder requestResponse, String tag) {
        List<Type> types = requestResponse.getTypes();
        if (types == null) {
            return false;
        }

        mTypeDao.saveOrUpdateDataSafe(types);
        for (Type type : types) {
            if (type != null) {
                if (type.isDeleted()) {
                    mTypeDao.deleteDataSafe(type.getId());
                }
            }
        }
        return true;
    }

    public List<Type> getTypes() {
        return mTypeDao.getAllSafe();
    }

    public void clear() {
        mTypeDao.deleteAll();
    }
}
