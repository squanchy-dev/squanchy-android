package com.ls.drupalconapp.model.managers;

import com.ls.drupal.AbstractBaseDrupalEntity;
import com.ls.drupal.DrupalClient;
import com.ls.drupalconapp.model.DatabaseManager;
import com.ls.drupalconapp.model.data.Track;
import com.ls.drupalconapp.model.requests.TracksRequest;

import java.util.List;

/**
 * Created on 09.06.2015.
 */
public class TracksManager extends SynchronousItemManager<Track.Holder,Object,String>{

    public TracksManager(DrupalClient client) {
        super(client);
    }

    @Override
    protected AbstractBaseDrupalEntity getEntityToFetch(DrupalClient client, Object requestParams) {
        return new TracksRequest(client);
    }

    @Override
    protected String getEntityRequestTag(Object params) {
        return "tracks";
    }

    @Override
    protected boolean storeResponse(Track.Holder requestResponse, String tag) {
        DatabaseManager databaseManager = DatabaseManager.instance();
        List<Track> tracks = requestResponse.getTracks();

        if (tracks == null) {
            return false;
        }

        databaseManager.saveTracks(tracks);

        for (Track track : tracks){
            if(track != null) {
                if (track.isDeleted()) {
                    databaseManager.deleteTrack(track);
                }
            }
        }
        return true;
    }
}
