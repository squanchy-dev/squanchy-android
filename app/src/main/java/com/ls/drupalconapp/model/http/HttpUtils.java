package com.ls.drupalconapp.model.http;

import android.net.Uri;

import com.ls.utils.DomenUtils;

/**
 * Created by Yakiv M. on 19.09.2014.
 */
public class HttpUtils {

    public static final String LIVE_SCHEME = "http";
    public static final String LIVE_AUTHORITY = "amsterdam2014.uat.link";

    public static final String mScheme = LIVE_SCHEME;
    public static final String mAuthority = DomenUtils.DEV_AUTHORITY;

    public static String createCheckUpdatesUrl() {
        Uri.Builder uri = createDefault();
        uri.path("api/v2/checkUpdates");
        return uri.build().toString();
    }

    public static String createTypesUrl() {
        Uri.Builder uri = createDefault();
        uri.path("api/v2/getTypes");

        return uri.build().toString();
    }

    public static String createSpeakersUrl() {
        Uri.Builder uri = createDefault();
        uri.path("api/v2/getSpeakers");

        return uri.build().toString();
    }

    public static String createLevelsUrl() {
        Uri.Builder uri = createDefault();
        uri.path("api/v2/getLevels");

        return uri.build().toString();
    }

    public static String createTracksUrl() {
        Uri.Builder uri = createDefault();
        uri.path("api/v2/getTracks");

        return uri.build().toString();
    }

    public static String createProgramsUrl() {
        Uri.Builder uri = createDefault();
        uri.path("api/v2/getSessions");

        return uri.build().toString();
    }

    public static String createBoFsUrl() {
        Uri.Builder uri = createDefault();
        uri.path("api/v2/getBofs");

        return uri.build().toString();
    }

    public static String createSocialsUrl() {
        Uri.Builder uri = createDefault();
        uri.path("api/v2/getSocialEvents");

        return uri.build().toString();
    }


    public static String createInfoUrl() {
        Uri.Builder uri = createDefault();
        uri.path("api/v2/getInfo");

        return uri.build().toString();
    }

    public static String createLocationsUrl() {
        Uri.Builder uri = createDefault();
        uri.path("api/v2/getLocations");

        return uri.build().toString();
    }

    public static String createHousePlansUrl() {
        Uri.Builder uri = createDefault();
        uri.path("api/v2/getHousePlans");

        return uri.build().toString();
    }

    public static String createPOIsUrl() {
        Uri.Builder uri = createDefault();
        uri.path("api/v2/getPOI");

        return uri.build().toString();
    }

    public static String createTwitterUrl() {
        Uri.Builder uri = createDefault();
        uri.path("api/v2/getTwitter");

        return uri.build().toString();
    }

    public static Uri.Builder createDefault() {
        Uri.Builder uri = new Uri.Builder();
        uri.scheme(mScheme);
        uri.authority(mAuthority);

        return uri;
    }
}
