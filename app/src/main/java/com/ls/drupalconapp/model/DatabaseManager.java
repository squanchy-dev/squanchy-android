package com.ls.drupalconapp.model;

import android.content.Context;

import com.ls.drupalconapp.model.dao.EventDao;
import com.ls.drupalconapp.model.dao.InfoDao;
import com.ls.drupalconapp.model.dao.LevelDao;
import com.ls.drupalconapp.model.dao.LocationDao;
import com.ls.drupalconapp.model.dao.POIDao;
import com.ls.drupalconapp.model.dao.SpeakerDao;
import com.ls.drupalconapp.model.dao.TrackDao;
import com.ls.drupalconapp.model.dao.TypeDao;
import com.ls.drupalconapp.model.data.Event;
import com.ls.drupalconapp.model.data.EventDetailsEvent;
import com.ls.drupalconapp.model.data.InfoItem;
import com.ls.drupalconapp.model.data.Level;
import com.ls.drupalconapp.model.data.Location;
import com.ls.drupalconapp.model.data.POI;
import com.ls.drupalconapp.model.data.Speaker;
import com.ls.drupalconapp.model.data.SpeakerDetailsEvent;
import com.ls.drupalconapp.model.data.TimeRange;
import com.ls.drupalconapp.model.data.Track;
import com.ls.drupalconapp.model.data.Type;
import com.ls.drupalconapp.model.database.ILAPIDBFacade;
import com.ls.drupalconapp.model.database.LAPIDBRegister;
import com.ls.drupalconapp.ui.adapter.item.EventListItem;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Yakiv M. on 22.09.2014.
 */
public class DatabaseManager {

    private static DatabaseManager instance;
    public static DatabaseManager instance(Context theContext)
    {
        if (instance == null)
        {
            instance = new DatabaseManager(theContext);
        }

        return instance;
    }


    public static DatabaseManager instance()
    {
        if (instance == null)
        {
            throw new IllegalStateException("Called method on uninitialized database manager");
        }

        return instance;
    }


    private TypeDao mTypeDao;
    private SpeakerDao mSpeakerDao;
    private LevelDao mLevelDao;
    private TrackDao mTrackDao;
    private LocationDao mLocationDao;
    private POIDao mPOIDao;
    private EventDao mEventDao;
    private InfoDao mInfoDao;

    private DatabaseManager(Context context) {
        mTypeDao = new TypeDao();
        mLevelDao = new LevelDao();
        mTrackDao = new TrackDao();
        mSpeakerDao = new SpeakerDao(context);
        mLocationDao = new LocationDao();
        mEventDao = new EventDao(context);
        mPOIDao = new POIDao();
        mInfoDao = new InfoDao();
    }

    public List<Type> getTypes() {
        return mTypeDao.getAllSafe();
    }

    public
    @Nullable
    Type getType(long typeId) {
        List<Type> data = mTypeDao.getDataSafe(typeId);
        return data.size() > 0 ? data.get(0) : null;
    }

    public void saveTypes(List<Type> data) {
        mTypeDao.saveOrUpdateDataSafe(data);
    }

    public void deleteType(Type type){
        mTypeDao.deleteDataSafe(type.getId());
    }

    public List<Speaker> getSpeakers() {
        return mSpeakerDao.selectSpeakersOrderedByName();
    }

    public List<Speaker> getSpeakers(long speakerId) {
        return mSpeakerDao.getDataSafe(speakerId);
    }

    public void deleteSpeaker(Speaker speaker){
        mSpeakerDao.deleteDataSafe(speaker.getId());
        mEventDao.deleteEventAndSpeakerBySpeaker(speaker.getId());
    }

    public void saveSpeakers(List<Speaker> data) {
        mSpeakerDao.saveOrUpdateDataSafe(data);
    }

    public List<Level> getLevels() {
        List<Level> levels = mLevelDao.getAllSafe();

        Collections.sort(levels, new Comparator<Level>() {
            @Override
            public int compare(Level level, Level level2) {
                return Double.compare(level.getOrder(), level2.getOrder());
            }
        });

        return levels;
    }

    public Level getLevel(long levelId) {
        List<Level> data = mLevelDao.getDataSafe(levelId);
        return data.size() > 0 ? data.get(0) : null;
    }

    public void saveLevels(List<Level> data) {
        mLevelDao.saveOrUpdateDataSafe(data);
    }

    public void deleteLevel(Level level) {
        mLevelDao.deleteDataSafe(level.getId());
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

    public void deleteTrack(Track track) {
        mTrackDao.deleteDataSafe(track.getId());
    }

    public Track getTrack(long trackId) {
        List<Track> data = mTrackDao.getDataSafe(trackId);
        return data.size() > 0 ? data.get(0) : null;
    }

    public void saveTracks(List<Track> data) {
        mTrackDao.saveOrUpdateDataSafe(data);
    }

    public List<Location> getLocations() {
        return mLocationDao.getAllSafe();
    }

    public void saveLocations(List<Location> data) {
        mLocationDao.saveOrUpdateDataSafe(data);
    }

    public void deleteLocation(Location location) {
        mLocationDao.deleteDataSafe(location.getId());
    }

    public void savePOIs(List<POI> data) {
        mPOIDao.saveOrUpdateDataSafe(data);
    }

    public void deletePOI(POI data) {
        mPOIDao.deleteDataSafe(data.getId());
    }

    public List<POI> getPOIs() {

        List<POI> pois = mPOIDao.getAllSafe();

        Collections.sort(pois, new Comparator<POI>() {
            @Override
            public int compare(POI poi, POI poi2) {
                return Double.compare(poi.getOrder(), poi2.getOrder());
            }
        });

        return pois;
    }

    public void saveInfo(List<InfoItem> data) {
        mInfoDao.saveOrUpdateDataSafe(data);
    }

    public void deleteInfo(InfoItem data) {
        mInfoDao.deleteDataSafe(data.getId());
    }

    public List<InfoItem> getInfo(){

        List<InfoItem> infoItems = mInfoDao.getAllSafe();

        Collections.sort(infoItems, new Comparator<InfoItem>() {
            @Override
            public int compare(InfoItem infoItem, InfoItem infoItem2) {
                return Double.compare(infoItem.getOrder(), infoItem2.getOrder());
            }
        });

        return infoItems;
    }

    public List<SpeakerDetailsEvent> getEventsBySpeakerId(long speakerId) {
        return mEventDao.getEventsBySpeakerId(speakerId);
    }

    public EventDetailsEvent getEventById(long id) {
        return mEventDao.getEventById(id);
    }

    public Speaker getSpeakerById(long id) {
        return mSpeakerDao.getSpeakerById(id).get(0);
    }

    public List<Event> getAllEvents() {
        return mEventDao.getAllSafe();
    }

    public List<Event> getEventsByIds(@NotNull List<Long> eventIds) {
        return mEventDao.selectEventsByIdsSafe(eventIds);
    }

    public List<Event> getEventsByIdsAndDay(@NotNull List<Long> eventIds, long day) {
        return mEventDao.selectEventsByIdsAndDaySafe(eventIds, day);
    }

    public List<Event> getEventsByDay(int eventClass, long date) {
        return mEventDao.selectEventsByDaySafe(eventClass, date);
    }

    public List<TimeRange> getTimeRangesDistinct(int eventClass, long date) {
        return mEventDao.selectDistrictTimeRangeSafe(eventClass, date);
    }

    public List<TimeRange> getTimeRangesDistinct(int eventClass, long date, List<Long> levelIds, List<Long> trackIds) {
        return mEventDao.selectDistrictTimeRangeByLevelTrackIdsSafe(eventClass, date, levelIds, trackIds);
    }

    public List<EventListItem> getEventItems(int eventClass, long date) {
        if (eventClass == Event.SOCIALS_CLASS) {
            return mEventDao.selectSocialItemsSafe(eventClass, date);
        }
        return mEventDao.selectBofsItemsSafe(eventClass, date);
    }

    public List<EventListItem> getProgramItemsByLevelTrackIds(int eventClass, long date, List<Long> levelIds, List<Long> trackIds) {
        return mEventDao.selectProgramItemsSafe(eventClass, date, levelIds, trackIds);
    }

    public List<TimeRange> getTimeRangesDistinctByEventIds(List<Long> eventIds) {
        return mEventDao.selectDistrictTimeRangeSafe(eventIds);
    }

    public List<Event> getBofses() {
        return mEventDao.selectBofsSafeSafe();
    }

    public List<Long> getEventDays() {
        return mEventDao.selectDistrictDateSafe();
    }

    public List<Long> getFavoriteEventDays() {
        return mEventDao.selectDistrictFavoriteDateSafe();
    }

    public List<Long> getProgramDays() {
        return mEventDao.selectDistrictDateSafe(Event.PROGRAM_CLASS);
    }

    public List<Long> getProgramDaysByLevelIds(List<Long> levelIds) {
        return mEventDao.selectDistrictDateByLevelIdsSafe(Event.PROGRAM_CLASS, levelIds);
    }

    public List<Long> getProgramDaysByTrackIds(List<Long> trackIds) {
        return mEventDao.selectDistrictDateByTrackIdsSafe(Event.PROGRAM_CLASS, trackIds);
    }

    public List<Long> getProgramDaysByTrackAndLevelIds(List<Long> levelIds, List<Long> trackIds) {
        return mEventDao.selectDistrictDateByTrackAndLevelIdsSafe(Event.PROGRAM_CLASS, levelIds, trackIds);
    }

    public List<Long> getBofsDays() {
        return mEventDao.selectDistrictDateSafe(Event.BOFS_CLASS);
    }

    public List<Long> getSocialsDays() {
        return mEventDao.selectDistrictDateSafe(Event.SOCIALS_CLASS);
    }

    public void saveEvent(Event data) {
        mEventDao.saveOrUpdateSafe(data);
    }

    public void deleteEvent(Event data) {
        mEventDao.deleteDataSafe(data.getId());
        mEventDao.deleteEventAndSpeakerByEvent(data.getId());
    }

    public void saveEventSpeakers(Event data) {
        Long eventId = data.getId();
        List<Long> speakerEventIds = getSpeakerEventIds();

        for (Long speakerId : data.getSpeakers()) {
            if (!speakerEventIds.contains(eventId)) {
                mEventDao.insertEventSpeaker(eventId, speakerId);
            }
        }
    }

    public List<Long> getSpeakerEventIds(){
        return mEventDao.selectSpeakerEventIds();
    }

    public List<Long> getEventSpeakers(long eventId) {
        return mEventDao.selectEventSpeakersSafe(eventId);
    }

    public List<Speaker> getSpeakersByEventId(long eventId) {
        return mSpeakerDao.getSpeakersByEventId(eventId);
    }

    public List<Long> getFavoriteEvents() {
        return mEventDao.selectFavoriteEventsSafe();
    }

    public void setFavoriteEvent(long eventId, boolean isFavorite) {
        mEventDao.setFavoriteSafe(eventId, isFavorite);
    }

    public void clearOldData() {
        mTypeDao.deleteAll();
        mSpeakerDao.deleteAll();
        mLevelDao.deleteAll();
        mTrackDao.deleteAll();
        mLocationDao.deleteAll();
        mEventDao.deleteAll();
        mPOIDao.deleteAll();
        mInfoDao.deleteAll();
    }

    public ILAPIDBFacade getFacade() {
        return LAPIDBRegister.getInstance().lookup(mTypeDao.getDatabaseName());
    }
}
