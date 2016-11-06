package com.ls.drupalcon.model;

import android.content.Context;
import android.os.Environment;

import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.NoCache;
import com.ls.drupal.DrupalClient;
import com.ls.drupalcon.R;
import com.ls.drupalcon.model.database.ILAPIDBFacade;
import com.ls.drupalcon.model.database.LAPIDBRegister;
import com.ls.drupalcon.model.http.hurl.HURLCookieStore;
import com.ls.drupalcon.model.http.hurl.RedirectHurlStack;
import com.ls.drupalcon.model.managers.BofsManager;
import com.ls.drupalcon.model.managers.EventManager;
import com.ls.drupalcon.model.managers.FavoriteManager;
import com.ls.drupalcon.model.managers.FloorPlansManager;
import com.ls.drupalcon.model.managers.InfoManager;
import com.ls.drupalcon.model.managers.LevelsManager;
import com.ls.drupalcon.model.managers.LocationManager;
import com.ls.drupalcon.model.managers.LoginManager;
import com.ls.drupalcon.model.managers.PoisManager;
import com.ls.drupalcon.model.managers.ProgramManager;
import com.ls.drupalcon.model.managers.SettingsManager;
import com.ls.drupalcon.model.managers.SocialManager;
import com.ls.drupalcon.model.managers.SpeakerManager;
import com.ls.drupalcon.model.managers.TracksManager;
import com.ls.drupalcon.model.managers.TypesManager;
import com.ls.http.base.BaseRequest;
import com.ls.http.base.ResponseData;

import java.io.File;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;

public class Model {

    private static final int CACHE_DISK_USAGE_BYTES = 20 * 1024 * 1024;
    private static final int REQUEST_TIMEOUT_MLLIS = 60 * 1000;

    private static Model instance;

    public static Model instance(Context theContext) {
        if (instance == null) {
            instance = new Model(theContext);
        }

        return instance;
    }

    public static Model instance() {
        if (instance == null) {
            throw new IllegalStateException("Called method on uninitialized model");
        }

        return instance;
    }

    private DrupalClient client;
    private LoginManager loginManager;
    private CookieStore cookieStore;
    private RequestQueue queue;

    /**
     * Managers
     */
    private TypesManager typesManager;
    private LevelsManager levelsManager;
    private TracksManager tracksManager;
    private SpeakerManager speakerManager;
    private LocationManager locationmanager;
    private SocialManager socialManager;
    private ProgramManager programManager;
    private BofsManager bofsManager;
    private PoisManager poisManager;
    private InfoManager infoManager;
    private EventManager eventManager;
    private UpdatesManager updatesManager;
    private FavoriteManager favoriteManager;
    private SettingsManager settingsManager;
    private FloorPlansManager floorPlansManager;

    public DrupalClient getClient() {
        return client;
    }

    public RequestQueue getQueue() {
        return queue;
    }

    public LoginManager getLoginManager() {
        return loginManager;
    }

    public CookieStore getCookieStore() {
        return cookieStore;
    }

    TypesManager getTypesManager() {
        return typesManager;
    }

    public LevelsManager getLevelsManager() {
        return levelsManager;
    }

    public TracksManager getTracksManager() {
        return tracksManager;
    }

    public SpeakerManager getSpeakerManager() {
        return speakerManager;
    }

    public LocationManager getLocationManager() {
        return locationmanager;
    }

    public SocialManager getSocialManager() {
        return socialManager;
    }

    public BofsManager getBofsManager() {
        return bofsManager;
    }

    PoisManager getPoisManager() {
        return poisManager;
    }

    public InfoManager getInfoManager() {
        return infoManager;
    }

    public ProgramManager getProgramManager() {
        return programManager;
    }

    ProgramManager createProgramManager(Context context) {
        return new ProgramManager(client, context);
    }

    public UpdatesManager getUpdatesManager() {
        return updatesManager;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public FavoriteManager getFavoriteManager() {
        return favoriteManager;
    }

    SettingsManager getSettingsManager() {
        return settingsManager;
    }

    public FloorPlansManager getFloorPlansManager() {
        return floorPlansManager;
    }

    public void setSettingsManager(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }

    /**
     * NOTE: login is performed in synchroneus way so you must never call it from UI thread.
     */
    public ResponseData performLogin(String userName, String password) {
        return this.loginManager.login(userName, password, queue);
    }

    private Model(Context context) {
        loginManager = new LoginManager();
        queue = createNoCachedQueue(context);
        client = new DrupalClient(context.getString(R.string.api_value_base_url), queue, BaseRequest.RequestFormat.JSON, loginManager);
        client.setRequestTimeout(REQUEST_TIMEOUT_MLLIS);

        typesManager = new TypesManager(client);
        levelsManager = new LevelsManager(client);
        tracksManager = new TracksManager(client);
        speakerManager = new SpeakerManager(client, context);
        locationmanager = new LocationManager(client);
        socialManager = new SocialManager(client, context);
        bofsManager = new BofsManager(client, context);
        poisManager = new PoisManager(client);
        infoManager = new InfoManager(client);
        programManager = new ProgramManager(client, context);
        eventManager = new EventManager(client, context);
        favoriteManager = new FavoriteManager(context);

        updatesManager = new UpdatesManager(client, context);
        settingsManager = new SettingsManager(client);
        floorPlansManager = new FloorPlansManager(client, context);
    }

    //Initialization

    public RequestQueue createNewQueue(Context context) {
        cookieStore = new HURLCookieStore(context);
        CookieManager cmrCookieMan = new CookieManager(cookieStore, CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cmrCookieMan);

        HttpStack stack = new RedirectHurlStack();
        return newRequestQueue(context, stack);
    }

    private RequestQueue createNoCachedQueue(Context context) {
        cookieStore = new HURLCookieStore(context);
        CookieManager cmrCookieMan = new CookieManager(cookieStore, CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cmrCookieMan);

        HttpStack stack = new RedirectHurlStack();
        return newNoCachedRequestQueue(stack);
    }

    /**
     * volley's default implementation uses internal cache only so we've implemented our, allowing external cache usage.
     */
    private static RequestQueue newRequestQueue(Context context, HttpStack stack) {

        File cacheDir;

        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            cacheDir = new File(context.getExternalCacheDir(), "volley");
        } else {
            cacheDir = new File(context.getCacheDir(), "volley");
        }

        if (stack == null) {
            stack = new HurlStack();
        }

        Network network = new BasicNetwork(stack);

        RequestQueue queue = new RequestQueue(new DiskBasedCache(cacheDir, CACHE_DISK_USAGE_BYTES), network, 1);
        queue.start();

        return queue;
    }

    private static RequestQueue newNoCachedRequestQueue(HttpStack stack) {
        if (stack == null) {
            stack = new HurlStack();
        }

        Network network = new BasicNetwork(stack);
        RequestQueue queue = new RequestQueue(new NoCache(), network, 1);
        queue.start();

        return queue;
    }

    protected void clearAllDao() {
        eventManager.clear();
        infoManager.clear();
        levelsManager.clear();
        locationmanager.clear();
        poisManager.clear();
        socialManager.clear();
        speakerManager.clear();
        tracksManager.clear();
        typesManager.clear();
    }

    ILAPIDBFacade getFacade() {
        return LAPIDBRegister.getInstance().lookup(AppDatabaseInfo.DATABASE_NAME);
    }
}
