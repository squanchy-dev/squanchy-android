package net.squanchy.service.firebase.model;

import java.util.Collections;
import java.util.Map;

import net.squanchy.support.lang.Optional;

public class FirebaseFavorites {

    public FirebaseFavorites() {
    }

    public FirebaseFavorites(Optional<Map<String, Boolean>> mapOptional) {
        this.map = mapOptional.or(Collections.emptyMap());
    }

    public Map<String, Boolean> map;

    public boolean hasFavorite(String eventId) {
        return map.containsKey(eventId);
    }
}
