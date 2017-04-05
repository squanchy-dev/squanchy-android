package net.squanchy.search.engines;

import java.util.List;

import net.squanchy.schedule.domain.view.Event;

public class QueryEngine implements Query{
    private final List<Query> queries;

    public QueryEngine(List<Query> queries) {
        this.queries = queries;
    }

    @Override
    public boolean matches(Event event, String query) {
        return false;
    }
}
