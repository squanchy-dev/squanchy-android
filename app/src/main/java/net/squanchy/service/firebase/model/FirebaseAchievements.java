package net.squanchy.service.firebase.model;

import java.util.Collections;
import java.util.Map;

import net.squanchy.support.lang.Optional;

public class FirebaseAchievements {

    public FirebaseAchievements() {
    }

    public FirebaseAchievements(Optional<Map<String, Long>> mapOptional) {
        this.achievements = mapOptional.or(Collections.emptyMap());
    }
    
    public Map<String, Long> achievements;
}
