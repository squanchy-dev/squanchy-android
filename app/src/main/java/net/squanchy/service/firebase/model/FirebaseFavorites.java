package net.squanchy.service.firebase.model;

import java.util.Collections;
import java.util.Map;

public class FirebaseFavorites {

    public FirebaseFavorites() {
    }

    public static Map<String, Boolean> empty() {
        return Collections.emptyMap();
    }

    public FirebaseFavorites(Map<String, Boolean> favorites) {
        this.favorites = favorites;
    }

    public Map<String, Boolean> favorites;

    public boolean hasFavorite(String eventId) {
        return favorites.containsKey(eventId);
    }
}
