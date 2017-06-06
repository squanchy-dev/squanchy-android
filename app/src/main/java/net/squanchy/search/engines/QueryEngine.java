package net.squanchy.search.engines;

import java.util.ArrayList;
import java.util.List;

import net.squanchy.schedule.domain.view.Event;
import net.squanchy.support.lang.Lists;

final class QueryEngine implements Query {

    private final List<Query> queries;

    QueryEngine(List<Query> queries) {
        this.queries = queries;
    }

    @Override
    public boolean matches(Event event, String queryParameter) {
        return Lists.any(queries, query -> query.matches(event, queryParameter));
    }

    public static class Builder {

        private final List<Query> queries = new ArrayList<>();

        Builder withQuery(Query query) {
            if (!queries.contains(query)) {
                queries.add(query);
            }
            return this;
        }

        QueryEngine build() {
            return new QueryEngine(queries);
        }
    }
}
