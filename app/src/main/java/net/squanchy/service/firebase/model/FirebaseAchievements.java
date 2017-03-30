package net.squanchy.service.firebase.model;

import java.util.Collections;
import java.util.Map;

public class FirebaseAchievements {

    public FirebaseAchievements() {
    }

    public static Map<String, Long> empty() {
        return Collections.emptyMap();
    }

    public FirebaseAchievements(Map<String, Long> achievements) {
        this.achievements = achievements;
    }

    public Map<String, Long> achievements;
}
