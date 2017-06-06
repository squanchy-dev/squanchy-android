package net.squanchy.schedule.view;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import net.squanchy.R;
import net.squanchy.schedule.domain.view.Event;

class EventsAdapter extends RecyclerView.Adapter<EventViewHolder> {

    private final LayoutInflater layoutInflater;

    @IntDef(value = {ItemViewType.TYPE_TALK, ItemViewType.TYPE_OTHER})
    @interface ItemViewType {

        int TYPE_TALK = 1;
        int TYPE_OTHER = 2;
    }

    private List<Event> events = Collections.emptyList();

    @Nullable
    private ScheduleViewPagerAdapter.OnEventClickedListener listener;

    EventsAdapter(Context context) {
        setHasStableIds(true);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public long getItemId(int position) {
        return events.get(position).getNumericId();
    }

    void updateWith(List<Event> events, ScheduleViewPagerAdapter.OnEventClickedListener listener) {
        this.events = events;
        this.listener = listener;
    }

    @Override
    @ItemViewType
    public int getItemViewType(int position) {
        Event.Type itemType = events.get(position).getType();
        switch (itemType) {
            case KEYNOTE:
            case TALK:
                return ItemViewType.TYPE_TALK;
            case COFFEE_BREAK:
            case LUNCH:
            case OTHER:
            case REGISTRATION:
            case SOCIAL:
                return ItemViewType.TYPE_OTHER;
            default:
                throw new IllegalArgumentException("Item of type " + itemType + " is not supported");
        }
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, @ItemViewType int viewType) {
        EventItemView itemView;
        if (viewType == ItemViewType.TYPE_TALK) {
            itemView = (EventItemView) layoutInflater.inflate(R.layout.item_schedule_event_talk, parent, false);
        } else if (viewType == ItemViewType.TYPE_OTHER) {
            itemView = (EventItemView) layoutInflater.inflate(R.layout.item_schedule_event_other, parent, false);
        } else {
            throw new IllegalArgumentException("View type not supported: " + viewType);
        }
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        holder.updateWith(events.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public List<Event> events() {
        return events;
    }
}
