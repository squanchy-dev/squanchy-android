package net.squanchy.ui.adapter.item;

import net.squanchy.model.data.Event;

public interface EventItemCreator {

    EventListItem getItem(Event event);

}
