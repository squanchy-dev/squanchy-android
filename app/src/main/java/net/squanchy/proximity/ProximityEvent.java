package net.squanchy.proximity;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ProximityEvent {

    private static final String NO_ACTION = "no_action";
    private static final String NO_SUBJECT = "no_subject";

    public static ProximityEvent create(String id, String action, String subject) {
        return new AutoValue_ProximityEvent(id, action, subject);
    }

    public static ProximityEvent create(String id) {
        return new AutoValue_ProximityEvent(id, NO_ACTION, NO_SUBJECT);
    }

    public abstract String id();

    public abstract String action();

    public abstract String subject();
}
