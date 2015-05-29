package com.ls.drupalconapp.model;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.ls.drupalconapp.model.data.Event;
import com.ls.drupalconapp.model.data.HousePlan;
import com.ls.drupalconapp.model.data.InfoItem;
import com.ls.drupalconapp.model.data.Level;
import com.ls.drupalconapp.model.data.Location;
import com.ls.drupalconapp.model.data.POI;
import com.ls.drupalconapp.model.data.Speaker;
import com.ls.drupalconapp.model.data.Track;
import com.ls.drupalconapp.model.data.Type;
import com.ls.drupalconapp.model.data.UpdateDate;
import com.ls.drupalconapp.model.database.ILAPIDBFacade;
import com.ls.drupalconapp.model.http.HttpFactory;
import com.ls.drupalconapp.model.http.RequestManager;
import com.ls.drupalconapp.ui.receiver.DataUpdateManager;
import com.ls.utils.L;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Yakiv M. on 19.09.2014.
 */
public class DownloadManager {

    /**
     * Note it should be equal to the number of async request that are proceeding at once.
     */
    private AtomicInteger mQueuedRequests;

    private Context mContext;
    private DownloadCallback mCallback;

    private UpdateDate mUpdateDate;
    private List<Type> mTypes;
    private List<Speaker> mSpeakers;
    private List<Level> mLevels;
    private List<Track> mTracks;
    private List<POI> mPois;
    private List<HousePlan> mHousePlans;
    private List<Event.Day> mProgramDays;
    private List<Event.Day> mBoFsDays;
    private List<Event.Day> mSocialsDays;
    private InfoItem.General mInfo;
    private List<Location> mLocations;
    private String mTwitter;

    int[] mRequestIds;
	List<Integer> mSkipIds = new ArrayList<>();

	private boolean mIsError;

    public void startLoading(@NotNull Context context, @NotNull DownloadCallback callback) {
        mContext = context;
        mCallback = callback;
		mSkipIds = new ArrayList<>();
		mQueuedRequests = new AtomicInteger(0);

        checkForUpdates();
    }

    private void checkForUpdates(){

        HttpFactory.createCheckUpdatesRequest(
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
						onCheckUpdatesLoaded(jsonObject);
                    }
                }, errorListener, PreferencesManager.getInstance().getLastUpdateDate()
        );

    }

    private void onCheckUpdatesLoaded(JSONObject jsonObject) {
		mUpdateDate = new Gson().fromJson(jsonObject.toString(), UpdateDate.class);
		try {
			JSONArray idsArray = jsonObject.getJSONArray("idsForUpdate");
			if (idsArray != null) {
				mRequestIds = new int[idsArray.length()];
				for (int i = 0; i < idsArray.length(); i++) {
					mRequestIds[i] = idsArray.getInt(i);
				}
			}

		} catch (JSONException e) {
			mRequestIds = null;
		}

		if (mRequestIds != null && mRequestIds.length == 0) {
			onDownloadSuccess(mUpdateDate);
		} else {
			loadData(mRequestIds);
		}
    }

    private void loadData(int[] idsArray) {
        for (int i : idsArray){
            sendRequestById(i);
        }
    }

    public void loadDataForUpdate(Context context, int[] idsArray, UpdateDate updateDate) {
        if(idsArray == null){
            return;
        }

        this.mContext = context;
        this.mRequestIds = idsArray;
        this.mUpdateDate = updateDate;

        for (int i : idsArray){
            sendRequestById(i);
        }
    }

    private void sendRequestById(int id){
		mQueuedRequests.incrementAndGet();
        switch (id){
            case HttpFactory.TYPES_REQUEST_ID:
                loadTypes();
                break;

            case HttpFactory.LEVELS_REQUEST_ID:
                loadLevels();
                break;

            case HttpFactory.TRACKS_REQUEST_ID:
                loadTracks();
                break;

            case HttpFactory.SPEAKERS_REQUEST_ID:
                loadSpeakers();
                break;

            case HttpFactory.LOCATIONS_REQUEST_ID:
                loadLocations();
                break;

            case HttpFactory.HOUSE_PLANS_REQUEST_ID:
                loadHousePlans();
                break;

            case HttpFactory.PROGRAMS_REQUEST_ID:
                loadPrograms();
                break;

            case HttpFactory.BOFS_REQUEST_ID:
                loadBoFses();
                break;

            case HttpFactory.SOCIALS_REQUEST_ID:
                loadSocials();
                break;

            case HttpFactory.POIS_REQUEST_ID:
                loadPOIs();
                break;

            case HttpFactory.INFO_REQUEST_ID:
                loadInfo();
                break;

            case HttpFactory.TWITTER_REQUEST_ID:
                loadTwitter();
                break;

        }
    }

    private void loadTypes() {
        HttpFactory.createTypesRequest(
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        onTypesLoaded(jsonObject);
                        checkLoadedData();
                    }
                }, errorListener, PreferencesManager.getInstance().getLastUpdateDate()
        );
    }

    private void onTypesLoaded(JSONObject jsonObject) {
        Type.Holder holder = JsonSerializable.fromJson(Type.Holder.class, jsonObject.toString());
		if (holder != null) {
			mTypes = holder.getTypes();
		}
    }

    private void loadLevels() {
        HttpFactory.createLevelsRequest(
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        onLevelsLoaded(jsonObject);
                        checkLoadedData();
                    }
                }, errorListener, PreferencesManager.getInstance().getLastUpdateDate()
        );
    }

	private void onLevelsLoaded(JSONObject jsonObject) {
		Level.Holder holder = JsonSerializable.fromJson(Level.Holder.class, jsonObject.toString());
		if (holder != null) {
			mLevels = holder.getLevels();
		}
	}

    private void loadTracks() {
        HttpFactory.createTracksRequest(
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        onTracksLoaded(jsonObject);
                        checkLoadedData();
                    }
                }, errorListener, PreferencesManager.getInstance().getLastUpdateDate()
        );
    }

    private void onTracksLoaded(JSONObject jsonObject) {
        Track.Holder holder = JsonSerializable.fromJson(Track.Holder.class, jsonObject.toString());
		if (holder != null) {
			mTracks = holder.getTracks();
		}
    }

    private void loadSpeakers() {
        HttpFactory.createSpeakersRequest(
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        onSpeakersLoaded(jsonObject);
                        checkLoadedData();
                    }
                }, errorListener, PreferencesManager.getInstance().getLastUpdateDate()
        );
    }

    private void onSpeakersLoaded(JSONObject jsonObject) {
        Speaker.Holder holder = JsonSerializable.fromJson(Speaker.Holder.class, jsonObject.toString());
		if (holder != null) {
			mSpeakers = holder.getSpeakers();
		}
    }

    private void loadLocations() {
        HttpFactory.createLocationsRequest(
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        onLocationsLoaded(jsonObject);
                        checkLoadedData();
                    }
                }, errorListener, PreferencesManager.getInstance().getLastUpdateDate()
        );
    }

	private void onLocationsLoaded(JSONObject jsonObject) {
		Location.Holder holder = JsonSerializable.fromJson(Location.Holder.class, jsonObject.toString());
		if (holder != null) {
			mLocations = holder.getLocations();
		}
	}

    private void loadHousePlans() {
        HttpFactory.createHousePlansRequest(
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        onHousePlansLoaded(jsonObject);
                        checkLoadedData();
                    }
                }, errorListener, PreferencesManager.getInstance().getLastUpdateDate()
        );
    }

    private void onHousePlansLoaded(JSONObject jsonObject) {
        mHousePlans = new ArrayList<HousePlan>(); //TODO: implement parsing of house plans data when this will be include to app
    }

    private void loadPrograms() {
        HttpFactory.createProgramsRequest(
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        onProgramsLoaded(jsonObject);
                        checkLoadedData();
                    }
                }, errorListener, PreferencesManager.getInstance().getLastUpdateDate()
        );
    }

    private void onProgramsLoaded(JSONObject jsonObject) {
        Event.Holder holder = JsonSerializable.fromJson(Event.Holder.class, jsonObject.toString());
		if (holder != null) {
			mProgramDays = holder.getDays();
		}
    }

    private void loadBoFses() {
        HttpFactory.createBoFsRequest(
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        onBoFsesLoaded(jsonObject);
                        checkLoadedData();
                    }
                }, errorListener, PreferencesManager.getInstance().getLastUpdateDate()
        );
    }

    private void onBoFsesLoaded(JSONObject jsonObject) {
        Event.Holder holder = JsonSerializable.fromJson(Event.Holder.class, jsonObject.toString());
		if(holder != null) {
			mBoFsDays = holder.getDays();
		}
	}

    private void loadSocials(){
        HttpFactory.createSocialsRequest(
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        onSocialsLoaded(jsonObject);
                        checkLoadedData();
                    }
                }, errorListener, PreferencesManager.getInstance().getLastUpdateDate()
        );
    }

    private void onSocialsLoaded(JSONObject jsonObject) {
        Event.Holder holder = JsonSerializable.fromJson(Event.Holder.class, jsonObject.toString());
		if (holder != null) {
			mSocialsDays = holder.getDays();
		}
    }

    private void loadPOIs(){
        HttpFactory.createPOIsRequest(
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        onPOIsLoaded(jsonObject);
                        checkLoadedData();
                    }
                }, errorListener, PreferencesManager.getInstance().getLastUpdateDate()
        );
    }

    private void onPOIsLoaded(JSONObject jsonObject) {
        POI.Holder holder = new Gson().fromJson(jsonObject.toString(), POI.Holder.class);
        mPois = holder.getPOIs();
    }

    private void loadInfo() {
        HttpFactory.createInfoRequest(
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        onInfoLoaded(jsonObject);
                        checkLoadedData();
                    }
                }, errorListener, PreferencesManager.getInstance().getLastUpdateDate()
        );
    }

    private void onInfoLoaded(JSONObject jsonObject) {
        mInfo = JsonSerializable.fromJson(InfoItem.General.class, jsonObject.toString());
    }

    private void loadTwitter() {
        HttpFactory.createTwitterRequest(
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        onTwitterLoaded(jsonObject);
                        checkLoadedData();
                    }
                }, errorListener, PreferencesManager.getInstance().getLastUpdateDate()
        );
    }

    private void onTwitterLoaded(JSONObject jsonObject) {
        mTwitter = "twitter"; //TODO: implement twitter html store when this will be added to the app
    }

    synchronized
    private void checkLoadedData() {
        if (mQueuedRequests.decrementAndGet() == 0) {

            new AsyncTask<Void, Void, Void>(){
                @Override
                protected Void doInBackground(Void... voids) {
                    saveData();
                    return null;
                }
            }.execute();

        }
    }

	private synchronized void saveData() {
		checkForSkipIds(mRequestIds);

		if (mContext == null) {
			onUnexpectedBehavior("Context is null in saveData()");
			onDownloadError();
			return;
		}
		DatabaseManager databaseManager = new DatabaseManager(mContext);
		ILAPIDBFacade facade = databaseManager.getFacade();

		try {
			facade.open();
			facade.beginTransactions();

			for (int id : mRequestIds) {
				if (!mSkipIds.contains(id)) {
					saveDataById(id, databaseManager);
				}
			}
//			PreferencesManager.getInstance().saveLastUpdateDate(mUpdateDate.getTime());

			facade.setTransactionSuccesfull();
		} finally {
			facade.endTransactions();
			facade.close();
		}
		DataUpdateManager.updateData(mContext, mRequestIds);
		onDownloadSuccess(mUpdateDate);
	}

    private void saveDataById(int id, DatabaseManager databaseManager){
        List<Long> ids = databaseManager.getFavoriteEvents();

        switch (id){
            case HttpFactory.TYPES_REQUEST_ID:
                databaseManager.saveTypes(mTypes);

                for (Type type : mTypes){
					if (type != null) {
						if (type.isDeleted()) {
							databaseManager.deleteType(type);
						}
					}
				}

                break;

            case HttpFactory.LEVELS_REQUEST_ID:
                databaseManager.saveLevels(mLevels);

                for (Level level : mLevels){
					if (level != null) {
						if (level.isDeleted()) {
							databaseManager.deleteLevel(level);
						}
					}
				}

                break;

            case HttpFactory.TRACKS_REQUEST_ID:
                databaseManager.saveTracks(mTracks);

                for (Track track : mTracks){
					if(track != null) {
						if (track.isDeleted()) {
							databaseManager.deleteTrack(track);
						}
					}
				}

                break;

            case HttpFactory.SPEAKERS_REQUEST_ID:
                databaseManager.saveSpeakers(mSpeakers);

                for (Speaker speaker : mSpeakers){
					if (speaker != null) {
						if (speaker.isDeleted()) {
							databaseManager.deleteSpeaker(speaker);
						}
					}
				}

                break;

            case HttpFactory.LOCATIONS_REQUEST_ID:
                databaseManager.saveLocations(mLocations);

                for (Location location : mLocations){
					if (location != null) {
						if (location.isDeleted()) {
							databaseManager.deleteLocation(location);
						}
					}
				}

                break;

            case HttpFactory.HOUSE_PLANS_REQUEST_ID:
                break;

            case HttpFactory.PROGRAMS_REQUEST_ID:
                saveEvents(databaseManager, mProgramDays, Event.PROGRAM_CLASS, ids);
                break;

            case HttpFactory.BOFS_REQUEST_ID:
                saveEvents(databaseManager, mBoFsDays, Event.BOFS_CLASS, ids);
                break;

            case HttpFactory.SOCIALS_REQUEST_ID:
                saveEvents(databaseManager, mSocialsDays, Event.SOCIALS_CLASS, ids);
                break;

            case HttpFactory.POIS_REQUEST_ID:
                databaseManager.savePOIs(mPois);

                for (POI poi : mPois){
                    if (poi.isDeleted()){
                        databaseManager.deletePOI(poi);
                    }
                }

                break;

            case HttpFactory.INFO_REQUEST_ID:
                databaseManager.saveInfo(mInfo.getInfo());

                for (InfoItem info : mInfo.getInfo()){
					if (info != null) {
						if (info.isDeleted()) {
							databaseManager.deleteInfo(info);
						}
					}
				}

                PreferencesManager.getInstance().saveMajorInfoTitle(mInfo.getMajorTitle());
                PreferencesManager.getInstance().saveMinorInfoTitle(mInfo.getMinorTitle());
                break;

            case HttpFactory.TWITTER_REQUEST_ID:
                break;

        }
    }

    private void saveEvents(DatabaseManager databaseManager, List<Event.Day> programDays,
            int eventClass, List<Long> ids) {

        SimpleDateFormat format = new SimpleDateFormat("d-MM-yyyy");

        for (Event.Day day : programDays) {
            for (Event event : day.getEvents()) {
				try {
					if (event != null) {
						Date date = format.parse(day.getDate());
						event.setDate(date);
						event.setEventClass(eventClass);

						for (long id : ids) {
							if (event.getId() == id) {
								event.setFavorite(true);
								break;
							}
						}

						databaseManager.saveEvent(event);
						databaseManager.saveEventSpeakers(event);

						if (event.isDeleted()) {
							databaseManager.deleteEvent(event);
						}

					}
				} catch (ParseException e) {
				}
			}
		}
    }

    private void checkForSkipIds(int[] requestIds) {
        for (int id : requestIds){
            Object dataObject = getDataObjectById(id);
            if (dataObject == null){
				mSkipIds.add(id);
            }
        }
    }

    private Object getDataObjectById(int id){

        Object dataObject = new Object();

        switch (id){
            case HttpFactory.TYPES_REQUEST_ID:
                dataObject = mTypes;
                break;

            case HttpFactory.LEVELS_REQUEST_ID:
                dataObject = mLevels;
                break;

            case HttpFactory.TRACKS_REQUEST_ID:
                dataObject = mTracks;
                break;

            case HttpFactory.SPEAKERS_REQUEST_ID:
                dataObject = mSpeakers;
                break;

            case HttpFactory.LOCATIONS_REQUEST_ID:
                dataObject = mLocations;
                break;

            case HttpFactory.HOUSE_PLANS_REQUEST_ID:
                dataObject = mHousePlans;
                break;

            case HttpFactory.PROGRAMS_REQUEST_ID:
                dataObject = mProgramDays;
                break;

            case HttpFactory.BOFS_REQUEST_ID:
                dataObject = mBoFsDays;
                break;

            case HttpFactory.SOCIALS_REQUEST_ID:
                dataObject = mSocialsDays;
                break;

            case HttpFactory.POIS_REQUEST_ID:
                dataObject = mPois;
                break;

            case HttpFactory.INFO_REQUEST_ID:
                dataObject = mInfo;
                break;

            case HttpFactory.TWITTER_REQUEST_ID:
                dataObject = mTwitter;
                break;

        }

        return dataObject;
    }

    private void onDownloadSuccess(UpdateDate date) {
        HttpFactory.isUpdating  = false;

        if (mCallback != null) {
			if (!mIsError) {
				mCallback.onDownloadSuccess(date);
			}else{
				mCallback.onDownloadSuccess(null);
			}
		}
    }

    private void onDownloadError() {
        if (mCallback != null) {
            mCallback.onDownloadError();
        }
    }

    private void onUnexpectedBehavior(String reason) {
        if (mCallback != null) {
            mCallback.onUnexpectedBehavior(reason);
        }
    }


    private boolean mIsCanceled;
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
			mIsError = true;
			checkLoadedData();
            L.e(volleyError.getMessage());
            if (!mIsCanceled) {
                // this is the workaround for case when server returns code 304, volley is not handling this, it returns volleyError == null;
                if (volleyError instanceof NoConnectionError || volleyError instanceof ServerError ||
                        volleyError instanceof TimeoutError) {

                    mIsCanceled = true;
                    HttpFactory.isUpdating  = false;
                    onDownloadError();
                    RequestManager.queue().cancelAll(new RequestQueue.RequestFilter() {
						@Override
						public boolean apply(Request<?> request) {
							return true;
						}
					});
                } else {
//					onDownloadSuccess(mUpdateDate);
				}
			}
        }
    };
}
