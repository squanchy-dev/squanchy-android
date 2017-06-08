package net.squanchy.favorites.view;

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
import net.squanchy.schedule.domain.view.Schedule;
import net.squanchy.schedule.domain.view.SchedulePage;
import net.squanchy.schedule.view.EventItemView;
import net.squanchy.schedule.view.EventViewHolder;
import net.squanchy.schedule.view.ScheduleViewPagerAdapter;
import net.squanchy.search.view.HeaderViewHolder;
import net.squanchy.support.lang.Lists;

import org.joda.time.LocalDateTime;

class FavoritesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final LayoutInflater layoutInflater;

    @IntDef(value = {ItemViewType.TYPE_TALK, ItemViewType.TYPE_HEADER})
    @interface ItemViewType {

        int TYPE_TALK = 1;
        int TYPE_HEADER = 2;
    }

    private Schedule schedule = Schedule.Companion.create(Collections.emptyList());

    @Nullable
    private ScheduleViewPagerAdapter.OnEventClickedListener listener;

    FavoritesAdapter(Context context) {
        setHasStableIds(true);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public long getItemId(int position) {
        return findFor(
                0,
                position,
                schedule.getPages(),
                schedulePage -> (long) -schedulePage.getDayId().hashCode(),
                (schedulePage, positionInPage) -> schedulePage.getEvents().get(positionInPage).getNumericId()
        );
    }

    void updateWith(Schedule schedule, ScheduleViewPagerAdapter.OnEventClickedListener listener) {
        this.schedule = schedule;
        this.listener = listener;
        notifyDataSetChanged();
    }

    @Override
    @ItemViewType
    public int getItemViewType(int position) {
        return findFor(
                0,
                position,
                schedule.getPages(),
                schedulePage -> ItemViewType.TYPE_HEADER,
                (schedulePage, positionInPage) -> ItemViewType.TYPE_TALK
        );
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, @ItemViewType int viewType) {
        if (viewType == ItemViewType.TYPE_TALK) {
            EventItemView itemView = (EventItemView) layoutInflater.inflate(R.layout.item_schedule_event_talk, parent, false);
            return new EventViewHolder(itemView);
        } else if (viewType == ItemViewType.TYPE_HEADER) {
            return new HeaderViewHolder(layoutInflater.inflate(R.layout.item_search_header, parent, false));
        } else {
            throw new IllegalArgumentException("View type not supported: " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EventViewHolder) {
            Event event = findFor(
                    0,
                    position,
                    schedule.getPages(),
                    schedulePage -> {
                        throw new IndexOutOfBoundsException();
                    },
                    (schedulePage, positionInPage) -> schedulePage.getEvents().get(positionInPage)
            );

            ((EventViewHolder) holder).updateWith(event, listener);
        } else if (holder instanceof HeaderViewHolder) {
            LocalDateTime date = findFor(
                    0,
                    position,
                    schedule.getPages(),
                    SchedulePage::getDate,
                    (schedulePage, positionInPage) -> {
                        throw new IndexOutOfBoundsException();
                    }
            );
            ((HeaderViewHolder) holder).updateWith(formatHeader(date));
        }
    }

    private CharSequence formatHeader(LocalDateTime date) {
        return date.toString("EEEE d");
    }

    @Override
    public int getItemCount() {
        return Lists.reduce(0, schedule.getPages(), (count, page) -> count + page.getEvents().size() + 1);
    }

    private <T> T findFor(int pagePosition, int position, List<SchedulePage> pages, Header<T> header, Row<T> row) {
        if (pagePosition >= pages.size()) {
            throw new IndexOutOfBoundsException();
        }

        SchedulePage schedulePage = pages.get(pagePosition);

        if (position == 0) {
            return header.get(schedulePage);
        }

        int adjustedPosition = position - 1;

        int size = schedulePage.getEvents().size();
        if (adjustedPosition < size) {
            return row.get(schedulePage, adjustedPosition);
        }

        return findFor(pagePosition + 1, adjustedPosition - size, pages, header, row);
    }

    interface Header<T> {

        T get(SchedulePage schedulePage);
    }

    interface Row<T> {

        T get(SchedulePage schedulePage, int positionInPage);
    }
}
