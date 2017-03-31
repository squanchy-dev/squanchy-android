package net.squanchy.support.system;

import org.joda.time.LocalDateTime;

public class CurrentTime {

    public Long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }

    public LocalDateTime getLocalDateTime() {
        return LocalDateTime.now();
    }
}
