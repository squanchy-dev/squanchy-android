package net.squanchy.search;

import com.google.auto.value.AutoValue;

import java.util.List;

import net.squanchy.schedule.domain.view.Event;
import net.squanchy.speaker.domain.view.Speaker;

@AutoValue
public abstract class SearchResults {

    public static SearchResults create(List<Event> events, List<Speaker> speakers) {
        return new AutoValue_SearchResults(events, speakers);
    }

    public abstract List<Event> events();

    public abstract List<Speaker> speakers();
}
