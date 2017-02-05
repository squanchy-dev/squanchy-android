package net.squanchy.model.managers;

import android.content.Context;

import net.squanchy.model.Model;
import net.squanchy.model.dao.TrackDao;
import net.squanchy.model.data.Level;
import net.squanchy.model.data.Track;
import net.squanchy.service.api.ConnfaRepository;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;

public class TracksManager extends SynchronousItemManager<Track.Holder, String> {

    private TrackDao mTrackDao;

    public TracksManager(Context context) {
        super(context);
        mTrackDao = new TrackDao(context);
    }

    @Override
    protected String getEntityRequestTag() {
        return "tracks";
    }

    @Override
    protected boolean storeResponse(Track.Holder requestResponse, String tag) {
        List<Track> tracks = requestResponse.getTracks();
        if (tracks == null) {
            return false;
        }

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

    @Override
    protected Observable<Track.Holder> doFetch(ConnfaRepository repository) {
        return repository.tracks();
    }

    public List<Track> getTracks() {
        List<Track> tracks = mTrackDao.getAllSafe();
        Collections.sort(tracks, (track, track2) -> Double.compare(track.getOrder(), track2.getOrder()));
        return tracks;
    }

    public Track getTrack(long trackId) {
        List<Track> data = mTrackDao.getDataSafe(trackId);
        return data.size() > 0 ? data.get(0) : null;
    }

    public List<Level> getLevels() {
        LevelsManager levelManager = Model.getInstance().getLevelsManager();

        List<Level> levels = levelManager.getLevels();
        Collections.sort(levels, (level, level2) -> Double.compare(level.getOrder(), level2.getOrder()));

        return levels;
    }

    public void clear() {
        mTrackDao.deleteAll();
    }
}
