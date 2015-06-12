package com.ls.drupalconapp.model.managers;

import com.ls.drupal.AbstractBaseDrupalEntity;
import com.ls.drupal.DrupalClient;
import com.ls.drupalconapp.model.dao.TrackDao;
import com.ls.drupalconapp.model.data.Track;
import com.ls.drupalconapp.model.requests.TracksRequest;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created on 09.06.2015.
 */
public class TracksManager extends SynchronousItemManager<Track.Holder, Object, String> {

    private TrackDao mTrackDao;

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
        List<Track> tracks = requestResponse.getTracks();
        if (tracks == null) return false;

        mTrackDao = new TrackDao();
        mTrackDao.saveOrUpdateDataSafe(tracks);

        for (Track track : tracks) {
            if (track != null) {
                if (track.isDeleted()) {
                    mTrackDao.deleteDataSafe(track.getId());
                }
            }
        }
        return true;
    }

    public List<Track> getTracks() {
        List<Track> tracks = mTrackDao.getAllSafe();
        Collections.sort(tracks, new Comparator<Track>() {
            @Override
            public int compare(Track track, Track track2) {
                return Double.compare(track.getOrder(), track2.getOrder());
            }
        });
        return tracks;
    }
}
