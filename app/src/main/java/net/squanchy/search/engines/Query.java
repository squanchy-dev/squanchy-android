package net.squanchy.search.engines;

import net.squanchy.schedule.domain.view.Event;

interface Query {

    boolean matches(Event event, String query);
}
