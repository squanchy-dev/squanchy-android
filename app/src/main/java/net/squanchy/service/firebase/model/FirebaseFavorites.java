package net.squanchy.service.firebase.model;

import java.util.Collections;
import java.util.Map;

public class FirebaseFavorites {

    public static FirebaseFavorites empty() {
        return new FirebaseFavorites(Collections.emptyMap());
    }

    public FirebaseFavorites() {
    }

    public FirebaseFavorites(Map<String, Boolean> map) {
        this.map = map;
    }

    public Map<String, Boolean> map;

    public boolean hasFavorite(String eventId) {
        return map.containsKey(eventId);
    }
}
