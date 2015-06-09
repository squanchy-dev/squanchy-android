package com.ls.drupalconapp.modelV2.managers;

import com.ls.drupal.AbstractBaseDrupalEntity;
import com.ls.drupal.DrupalClient;
import com.ls.drupalconapp.model.DatabaseManager;
import com.ls.drupalconapp.model.data.Type;
import com.ls.drupalconapp.modelV2.requests.TypesRequest;

import java.util.List;

/**
 * Created on 08.06.2015.
 */
public class TypesManager extends SynchronousItemManager<Type.Holder,Object,String> {

    public TypesManager(DrupalClient client) {
        super(client);
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
        DatabaseManager databaseManager = DatabaseManager.instance();
        List<Type> types = requestResponse.getTypes();

        if(types == null)
        {
            return false;
        }

        databaseManager.saveTypes(types);

        for (Type type : types){
            if (type != null) {
                if (type.isDeleted()) {
                    databaseManager.deleteType(type);
                }
            }
        }
        return true;
    }
}
