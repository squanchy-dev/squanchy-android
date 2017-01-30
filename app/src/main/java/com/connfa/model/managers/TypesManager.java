package com.connfa.model.managers;

import android.content.Context;

import com.connfa.model.dao.TypeDao;
import com.connfa.model.data.Type;
import com.connfa.service.api.ConnfaRepository;

import java.util.List;

import io.reactivex.Observable;

public class TypesManager extends SynchronousItemManager<Type.Holder, String> {

    private TypeDao mTypeDao;

    public TypesManager(Context context) {
        super(context);
        mTypeDao = new TypeDao(context);
    }

    @Override
    protected String getEntityRequestTag() {
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

    @Override
    protected Observable<Type.Holder> doFetch(ConnfaRepository repository) {
        return repository.types();
    }

    public List<Type> getTypes() {
        return mTypeDao.getAllSafe();
    }

    public void clear() {
        mTypeDao.deleteAll();
    }
}
