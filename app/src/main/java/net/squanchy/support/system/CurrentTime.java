package net.squanchy.support.system;

import org.joda.time.LocalDateTime;

public class CurrentTime {

    public Long currentTimestamp() {
        return System.currentTimeMillis();
    }

    public LocalDateTime currentLocalDateTime() {
        return LocalDateTime.now();
    }
}
