package net.squanchy.proximity;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ProximityEvent {

    public static ProximityEvent create(String action, String subject) {
        return new AutoValue_ProximityEvent(action, subject);
    }

    public abstract String action();

    public abstract String subject();
}
