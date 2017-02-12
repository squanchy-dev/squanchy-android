package net.squanchy.schedule.view;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import net.squanchy.schedule.domain.view.Event;

class EventViewHolder extends RecyclerView.ViewHolder {

    EventViewHolder(EventItemView itemView) {
        super(itemView);
    }

    void updateWith(Event event, @Nullable ScheduleViewPagerAdapter.OnEventClickedListener listener) {
        ((EventItemView) itemView).updateWith(event);
        itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEventClicked(event);
            }
        });
    }
}
