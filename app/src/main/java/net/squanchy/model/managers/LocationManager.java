package net.squanchy.model.managers;

import android.content.Context;

import net.squanchy.model.dao.LocationDao;
import net.squanchy.model.data.Location;
import net.squanchy.service.api.SquanchyRepository;

import java.util.List;

import io.reactivex.Observable;

public class LocationManager extends SynchronousItemManager<Location.Holder, String> {

    private LocationDao mLocationDao;

    public LocationManager(Context context) {
        super(context);
        mLocationDao = new LocationDao(context);
    }

    @Override
    protected String getEntityRequestTag() {
        return "location";
    }

    @Override
    protected boolean storeResponse(Location.Holder requestResponse, String tag) {
        List<Location> locations = requestResponse.getLocations();
        if (locations == null) {
            return false;
        }

        mLocationDao.saveOrUpdateDataSafe(locations);
        for (Location location : locations) {
            if (location != null) {
                if (location.isDeleted()) {
                    mLocationDao.deleteDataSafe(location.getId());
                }
            }
        }
        return true;
    }

    @Override
    protected Observable<Location.Holder> doFetch(SquanchyRepository repository) {
        return repository.locations();
    }

    public List<Location> getLocations() {
        return mLocationDao.getAllSafe();
    }

    public void clear() {
        mLocationDao.deleteAll();
    }
}
