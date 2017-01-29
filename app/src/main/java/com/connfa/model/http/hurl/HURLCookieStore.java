package com.connfa.model.http.hurl;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import timber.log.Timber;

/**
 * @deprecated this should really be the HTTP client's job
 */
public class HURLCookieStore implements CookieStore {

    private static final String COOKIE_PREFS_FILENAME = "cookies-store";

    private final SharedPreferences preferences;
    private final Map<URI, List<HttpCookie>> cookiesMap;

    public static HURLCookieStore newInstance(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(COOKIE_PREFS_FILENAME, 0);

        Map<URI, List<HttpCookie>> cookiesMap = new HashMap<>();
        Map<String, ?> preferencesMap = preferences.getAll();
        for (String preferenceKey : preferencesMap.keySet()) {
            addToMap(preferences, preferenceKey, cookiesMap);
        }

        return new HURLCookieStore(preferences, cookiesMap);
    }

    private HURLCookieStore(SharedPreferences preferences, Map<URI, List<HttpCookie>> cookiesMap) {
        this.preferences = preferences;
        this.cookiesMap = cookiesMap;
    }

    private static void addToMap(SharedPreferences preferences, String key, Map<URI, List<HttpCookie>> cookiesMap) {
        URI uri;
        try {
            uri = new URI(key);
        } catch (URISyntaxException e) {
            Timber.log(Log.WARN, e, "Error parsing URI: %s", key);
            return;
        }

        String rawCookie = preferences.getString(key, null);
        if (rawCookie == null) {
            removeFromPreferences(preferences, key);
            return;
        }
        addToMap(uri, rawCookie, cookiesMap);
    }

    private static void removeFromPreferences(SharedPreferences preferences, String key) {
        preferences.edit()
                .remove(key)
                .apply();
    }

    private static void addToMap(URI uri, String rawCookie, Map<URI, List<HttpCookie>> cookiesMap) {
        List<HttpCookie> cookies;
        if (cookiesMap.containsKey(uri)) {
            cookies = cookiesMap.get(uri);
        } else {
            cookies = new ArrayList<>();
        }
        cookies.addAll(HttpCookie.parse(rawCookie));
        cookiesMap.put(uri, cookies);
    }

    /*
     * @see java.net.CookieStore#add(java.net.URI, java.net.HttpCookie)
     */
    public void add(URI uri, HttpCookie cookie) {
        List<HttpCookie> cookies = cookiesMap.get(uri);
        if (cookies == null) {
            cookies = new ArrayList<>();
            cookiesMap.put(uri, cookies);
        }
        cookies.add(cookie);
        HashSet<String> setCookies = new HashSet<>();
        setCookies.add(cookie.toString());
        Set<String> stringSet = preferences.getStringSet(uri.toString(), setCookies);
        preferences.edit()
                .putStringSet(uri.toString(), stringSet)
                .apply();
    }

    /*
     * @see java.net.CookieStore#get(java.net.URI)
     */
    public List<HttpCookie> get(URI uri) {
        List<HttpCookie> lstCookies = cookiesMap.get(uri);
        if (lstCookies == null) {
            cookiesMap.put(uri, new ArrayList<>());
        }
        return cookiesMap.get(uri);
    }

    /*
     * @see java.net.CookieStore#removeAll()
     */
    public boolean removeAll() {
        cookiesMap.clear();
        return true;
    }

    /*
     * @see java.net.CookieStore#getCookies()
     */
    public List<HttpCookie> getCookies() {
        Collection<List<HttpCookie>> values = cookiesMap.values();
        List<HttpCookie> result = new ArrayList<>();
        for (List<HttpCookie> value : values) {
            result.addAll(value);
        }
        return result;
    }

    /*
     * @see java.net.CookieStore#getURIs()
     */
    public List<URI> getURIs() {
        Set<URI> keys = cookiesMap.keySet();
        return new ArrayList<>(keys);
    }

    /*
     * @see java.net.CookieStore#remove(java.net.URI, java.net.HttpCookie)
     */
    public boolean remove(URI uri, HttpCookie cookie) {
        List<HttpCookie> cookies = cookiesMap.get(uri);
        if (cookies == null) {
            return false;
        }
        return cookies.remove(cookie);
    }
}
