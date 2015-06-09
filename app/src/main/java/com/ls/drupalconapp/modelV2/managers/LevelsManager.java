package com.ls.drupalconapp.modelV2.managers;

import com.ls.drupal.AbstractBaseDrupalEntity;
import com.ls.drupal.DrupalClient;
import com.ls.drupalconapp.model.DatabaseManager;
import com.ls.drupalconapp.model.data.Level;
import com.ls.drupalconapp.model.data.Type;
import com.ls.drupalconapp.modelV2.requests.LevelsRequest;

import java.util.List;

/**
 * Created on 09.06.2015.
 */
public class LevelsManager  extends SynchronousItemManager<Level.Holder,Object,String> {

    public LevelsManager(DrupalClient client) {
        super(client);
    }

    @Override
    protected AbstractBaseDrupalEntity getEntityToFetch(DrupalClient client, Object requestParams) {
        return new LevelsRequest(client);
    }

    @Override
    protected String getEntityRequestTag(Object params) {
        return "levels";
    }

    @Override
    protected boolean storeResponse(Level.Holder requestResponse, String tag) {
        DatabaseManager databaseManager = DatabaseManager.instance();
        List<Level> levels = requestResponse.getLevels();
        if(levels == null)
        {
            return false;
        }
        databaseManager.saveLevels(levels);

        for (Level level : levels){
            if (level != null) {
                if (level.isDeleted()) {
                    databaseManager.deleteLevel(level);
                }
            }
        }
        return true;
    }
}
