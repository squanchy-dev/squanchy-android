package net.squanchy.model;

import android.content.Context;
import android.os.Environment;

import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.NoCache;
import net.squanchy.R;
import net.squanchy.model.database.ILAPIDBFacade;
import net.squanchy.model.database.LAPIDBRegister;
import net.squanchy.model.http.hurl.HURLCookieStore;
import net.squanchy.model.http.hurl.RedirectHurlStack;
import net.squanchy.model.managers.BofsManager;
import net.squanchy.model.managers.EventManager;
import net.squanchy.model.managers.FavoriteManager;
import net.squanchy.model.managers.FloorPlansManager;
import net.squanchy.model.managers.InfoManager;
import net.squanchy.model.managers.LevelsManager;
import net.squanchy.model.managers.LocationManager;
import net.squanchy.model.managers.LoginManager;
import net.squanchy.model.managers.PoisManager;
import net.squanchy.model.managers.ProgramManager;
import net.squanchy.model.managers.SettingsManager;
import net.squanchy.model.managers.SocialManager;
import net.squanchy.model.managers.SpeakerManager;
import net.squanchy.model.managers.TracksManager;
import net.squanchy.model.managers.TypesManager;
import net.squanchy.service.api.ConnfaRepository;
import com.ls.drupal.DrupalClient;
import com.ls.http.base.BaseRequest;

import java.io.File;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;

public class Model {

    private static final int CACHE_DISK_USAGE_BYTES = 20 * 1024 * 1024;
    private static final int REQUEST_TIMEOUT_MLLIS = 60 * 1000;

    private static Model instance;

    private DrupalClient client;
    private CookieStore cookieStore;

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

    public static void createInstance(Context context, ConnfaRepository repository) {
        if (instance != null) {
            throw new IllegalStateException("Instance already initialized.");
        }
        instance = new Model(context, repository);
    }

    public static Model getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Instance not initialized yet. Make sure you call createInstance() first.");
        }

        return instance;
    }

    public DrupalClient getClient() {
        return client;
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
        return new ProgramManager(context);
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

    private Model(Context context, ConnfaRepository repository) {
        LoginManager loginManager = new LoginManager();
        RequestQueue queue = createNoCachedQueue(context);
        client = new DrupalClient(context.getString(R.string.api_value_base_url), queue, BaseRequest.RequestFormat.JSON, loginManager);
        client.setRequestTimeout(REQUEST_TIMEOUT_MLLIS);

        typesManager = new TypesManager(context);
        levelsManager = new LevelsManager(context);
        tracksManager = new TracksManager(context);
        speakerManager = new SpeakerManager(context);
        locationmanager = new LocationManager(context);
        socialManager = new SocialManager(context);
        bofsManager = new BofsManager(context);
        poisManager = new PoisManager(context);
        infoManager = new InfoManager(context);
        programManager = new ProgramManager(context);
        eventManager = new EventManager(context);
        favoriteManager = new FavoriteManager(context);

        updatesManager = new UpdatesManager(repository);
        settingsManager = new SettingsManager(context);
        floorPlansManager = new FloorPlansManager(context, client);
    }

    //Initialization

    public RequestQueue createNewQueue(Context context) {
        cookieStore = HURLCookieStore.newInstance(context);
        CookieManager cmrCookieMan = new CookieManager(cookieStore, CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cmrCookieMan);

        HttpStack stack = new RedirectHurlStack();
        return newRequestQueue(context, stack);
    }

    private RequestQueue createNoCachedQueue(Context context) {
        cookieStore = HURLCookieStore.newInstance(context);
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

    ILAPIDBFacade getFacade() {
        return LAPIDBRegister.getInstance().lookup(AppDatabaseInfo.DATABASE_NAME);
    }
}
