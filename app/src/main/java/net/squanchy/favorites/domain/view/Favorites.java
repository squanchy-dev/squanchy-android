package net.squanchy.favorites.domain.view;

import com.google.auto.value.AutoValue;

import java.util.List;

import net.squanchy.schedule.domain.view.Event;

@AutoValue
public abstract class Favorites {

    public static Favorites create(List<Event> events) {
        return new AutoValue_Favorites(events);
    }

    public abstract List<Event> events();
}
