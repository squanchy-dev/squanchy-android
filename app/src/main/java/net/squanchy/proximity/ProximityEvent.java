package net.squanchy.proximity;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ProximityEvent {

    public static ProximityEvent create(String id, String action, String subject) {
        return new AutoValue_ProximityEvent(id, action, subject);
    }

    public abstract String id();

    public abstract String action();

    public abstract String subject();
}
