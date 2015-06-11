package com.ls.drupalconapp.model.http;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.ls.drupalconapp.ui.drawer.DrawerManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yakiv M. on 19.09.2014.
 */
@Deprecated //Use requests instead
public class HttpFactory {

    public static final String CHECK_UPDATES_REQUEST = "check_updates";
    public static final String TYPES_REQUEST = "types";
    public static final String LEVELS_REQUEST = "levels";
    public static final String TRACKS_REQUEST = "tracks";
    public static final String SPEAKERS_REQUEST = "speakers";
    public static final String LOCATIONS_REQUEST = "locations";
    public static final String HOUSE_PLANS_REQUEST = "house_plans";
    public static final String PROGRAMS_REQUEST = "programs";
    public static final String BOFS_REQUEST = "bofs";
    public static final String SOCIALS_REQUEST = "socials";
    public static final String POIS_REQUEST = "poi";
    public static final String INFO_REQUEST = "info";
    public static final String TWITTER_REQUEST = "twitter";

    public static final int TYPES_REQUEST_ID = 1;
    public static final int LEVELS_REQUEST_ID = 2;
    public static final int TRACKS_REQUEST_ID = 3;
    public static final int SPEAKERS_REQUEST_ID = 4;
    public static final int LOCATIONS_REQUEST_ID = 5;
    public static final int HOUSE_PLANS_REQUEST_ID = 6;
    public static final int PROGRAMS_REQUEST_ID = 7;
    public static final int BOFS_REQUEST_ID = 8;
    public static final int SOCIALS_REQUEST_ID = 9;
    public static final int POIS_REQUEST_ID = 10;
    public static final int INFO_REQUEST_ID = 11;
    public static final int TWITTER_REQUEST_ID = 12;

    public static final String IF_MODIFIED_SINCE_HEADER= "If-Modified-Since";
    public static final String LAST_MODIFIED_HEADER= "Last-Modified";
    public static boolean isUpdating = false;

    public static void createCheckUpdatesRequest(Response.Listener<JSONObject> listener,
            Response.ErrorListener errorListener, String ifModifiedHeader) {
        createRequest(HttpUtils.createCheckUpdatesUrl(), CHECK_UPDATES_REQUEST, listener, errorListener, ifModifiedHeader);
    }

    public static void createTypesRequest(Response.Listener<JSONObject> listener,
            Response.ErrorListener errorListener, String ifModifiedHeader) {
        createRequest(HttpUtils.createTypesUrl(), TYPES_REQUEST, listener, errorListener, ifModifiedHeader);
    }

    public static void createSpeakersRequest(Response.Listener<JSONObject> listener,
            Response.ErrorListener errorListener, String ifModifiedHeader) {
        createRequest(HttpUtils.createSpeakersUrl(), SPEAKERS_REQUEST, listener, errorListener, ifModifiedHeader);
    }

    public static void createLevelsRequest(Response.Listener<JSONObject> listener,
            Response.ErrorListener errorListener, String ifModifiedHeader) {
        createRequest(HttpUtils.createLevelsUrl(), LEVELS_REQUEST, listener, errorListener, ifModifiedHeader);
    }

    public static void createTracksRequest(Response.Listener<JSONObject> listener,
            Response.ErrorListener errorListener, String ifModifiedHeader) {
        createRequest(HttpUtils.createTracksUrl(), TRACKS_REQUEST, listener, errorListener, ifModifiedHeader);
    }

    public static void createProgramsRequest(Response.Listener<JSONObject> listener,
            Response.ErrorListener errorListener, String ifModifiedHeader) {
        createRequest(HttpUtils.createProgramsUrl(), PROGRAMS_REQUEST, listener, errorListener, ifModifiedHeader);
    }

    public static void createBoFsRequest(Response.Listener<JSONObject> listener,
            Response.ErrorListener errorListener, String ifModifiedHeader) {
        createRequest(HttpUtils.createBoFsUrl(), BOFS_REQUEST, listener, errorListener, ifModifiedHeader);
    }

    public static void createSocialsRequest(Response.Listener<JSONObject> listener,
                                         Response.ErrorListener errorListener, String ifModifiedHeader) {
        createRequest(HttpUtils.createSocialsUrl(), SOCIALS_REQUEST, listener, errorListener, ifModifiedHeader);
    }

    public static void createInfoRequest(Response.Listener<JSONObject> listener,
                                          Response.ErrorListener errorListener, String ifModifiedHeader) {
        createRequest(HttpUtils.createInfoUrl(), INFO_REQUEST, listener, errorListener, ifModifiedHeader);
    }

    public static void createTwitterRequest(Response.Listener<JSONObject> listener,
                                         Response.ErrorListener errorListener, String ifModifiedHeader) {
        createRequest(HttpUtils.createTwitterUrl(), TWITTER_REQUEST, listener, errorListener, ifModifiedHeader);
    }

    public static void createLocationsRequest(Response.Listener<JSONObject> listener,
            Response.ErrorListener errorListener, String ifModifiedHeader) {
        createRequest(HttpUtils.createLocationsUrl(), LOCATIONS_REQUEST, listener, errorListener, ifModifiedHeader);
    }

    public static void createHousePlansRequest(Response.Listener<JSONObject> listener,
                                              Response.ErrorListener errorListener, String ifModifiedHeader) {
        createRequest(HttpUtils.createHousePlansUrl(), HOUSE_PLANS_REQUEST, listener, errorListener, ifModifiedHeader);
    }

    public static void createPOIsRequest(Response.Listener<JSONObject> listener,
                                              Response.ErrorListener errorListener, String ifModifiedHeader) {
        createRequest(HttpUtils.createPOIsUrl(), POIS_REQUEST, listener, errorListener, ifModifiedHeader);
    }


    private static void createRequest(String url, String tag,
        Response.Listener<JSONObject> listener,
        Response.ErrorListener errorListener, final String ifModifiedHeader) {
        Request request = new DCJsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                listener,
                errorListener){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(IF_MODIFIED_SINCE_HEADER, ifModifiedHeader);
                return map;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {

                try {

                    if (response.notModified){ // workaround for volley bug sometimes it handles 304 code, sometimes not
                        return Response.error(new ParseError(new NullPointerException()));
                    }

                    String string = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    JSONObject responseJson = new JSONObject(string);
                    responseJson.put(LAST_MODIFIED_HEADER, response.headers.get(LAST_MODIFIED_HEADER));
                    return Response.success(responseJson,HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e){
                    return Response.error(new ParseError(e));
                } catch (JSONException e){
                    return Response.error(new ParseError(e));
                }
            }
        };

        request.setTag(tag);
        RequestManager.queue().add(request);
    }

	public static int convertEventIdToEventModePos(int eventModePos) {
		switch (eventModePos) {
			case PROGRAMS_REQUEST_ID:
				return DrawerManager.EventMode.Program.ordinal();
			case BOFS_REQUEST_ID:
				return DrawerManager.EventMode.Bofs.ordinal();
			case SOCIALS_REQUEST_ID:
				return DrawerManager.EventMode.Social.ordinal();
		}
		return 0;
	}
}
