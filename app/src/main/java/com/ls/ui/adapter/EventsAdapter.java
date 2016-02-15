package com.ls.ui.adapter;

import com.ls.drupalcon.R;
import com.ls.drupalcon.app.App;
import com.ls.drupalcon.model.data.Event;
import com.ls.drupalcon.model.data.Level;
import com.ls.drupalcon.model.data.Type;
import com.ls.ui.adapter.item.BofsItem;
import com.ls.ui.adapter.item.EventListItem;
import com.ls.ui.adapter.item.HeaderItem;
import com.ls.ui.adapter.item.ProgramItem;
import com.ls.ui.adapter.item.SocialItem;
import com.ls.ui.adapter.item.TimeRangeItem;
import com.ls.ui.drawer.DrawerManager;
import com.ls.utils.DateUtils;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EventsAdapter extends BaseAdapter {

    private static final int TYPE_COUNT = 5;
    private static final int SINGLE_LINE_COUNT = 1;
    private static final int MULTI_LINE_COUNT = 3;

    private Context mContext;
    private List<EventListItem> mData;
    private LayoutInflater mInflater;

    private DrawerManager.EventMode mEventMode;
    private Listener mListener;

    public interface Listener {
        void onClick(int position);
    }

    public EventsAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mData = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getAdapterType();
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public EventListItem getItem(int position) {
        return this.mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setData(List<EventListItem> data, DrawerManager.EventMode mode) {
        mData.clear();
        mData.addAll(data);
        mEventMode = mode;
        notifyDataSetChanged();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View resultView;
        int itemViewType = getItemViewType(position);

        if (itemViewType == EventListItem.TYPE_TIME_RANGE) {
            resultView = initTimeRangeView(position, convertView, parent);
        } else if (itemViewType == EventListItem.TYPE_BOFS) {
            resultView = initBofsView(position, convertView, parent);
        } else if (itemViewType == EventListItem.TYPE_PROGRAM) {
            resultView = initProgramView(position, convertView, parent);
        } else if (itemViewType == EventListItem.TYPE_SOCIAL) {
            resultView = initSocialView(position, convertView, parent);
        } else if (itemViewType == EventListItem.TYPE_SECTION_NAME) {
            resultView = initSectionNameView(position, convertView, parent);
        } else {
            resultView = new View(mInflater.getContext());
        }

        return resultView;
    }

    public void setOnItemClickListener(Listener listener) {
        mListener = listener;
    }

    public View initTimeRangeView(final int position, View convertView, ViewGroup parent) {
        View resultView = convertView;
        EventHolder holder;

        if (resultView == null) {
            resultView = mInflater.inflate(R.layout.item_event, parent, false);
            holder = createEventHolder(resultView);
            resultView.setTag(holder);
        } else {
            holder = (EventHolder) resultView.getTag();
        }

        TimeRangeItem timeRange = (TimeRangeItem) getItem(position);
        Event event = timeRange.getEvent();

        fillDate(holder, event);
        fillIcon(holder, event.getType());
        fillEventInfo(holder, event, timeRange.getTrack(), timeRange.getSpeakers());
        fillDivider(holder, timeRange.isFirst());
        fillFavorite(holder);
        fillEventClickAbility(holder.layoutRoot, holder.txtPlace, event, position);

        return resultView;
    }

    public View initBofsView(final int position, View convertView, ViewGroup parent) {
        View resultView = convertView;
        EventHolder holder;

        if (resultView == null) {
            resultView = mInflater.inflate(R.layout.item_event, parent, false);
            holder = createEventHolder(resultView);
            resultView.setTag(holder);
        } else {
            holder = (EventHolder) resultView.getTag();
        }

        BofsItem bofsItem = (BofsItem) getItem(position);
        Event event = bofsItem.getEvent();

        fillIcon(holder, event.getType());
        fillEventInfo(holder, event, null, bofsItem.getSpeakers());
        fillEventClickAbility(holder.layoutRoot, holder.txtPlace, event, position);
        fillDivider(holder, !bofsItem.isLast());

        return resultView;
    }

    public View initProgramView(final int position, View convertView, ViewGroup parent) {
        View resultView = convertView;
        EventHolder holder;

        if (resultView == null) {
            resultView = mInflater.inflate(R.layout.item_event, parent, false);
            holder = createEventHolder(resultView);
            resultView.setTag(holder);

            resultView.setTag(holder);
        } else {
            holder = (EventHolder) resultView.getTag();
        }

        ProgramItem programItem = (ProgramItem) getItem(position);
        Event event = programItem.getEvent();

        fillEventInfo(holder, event, programItem.getTrack(), programItem.getSpeakers());
        fillIcon(holder, event.getType());
        fillDivider(holder, !programItem.isLast());
        fillEventClickAbility(holder.layoutRoot, holder.txtPlace, event, position);

        return resultView;
    }

    private View initSocialView(final int position, View convertView, ViewGroup parent) {
        View resultView = convertView;
        EventHolder holder;

        if (resultView == null) {
            resultView = mInflater.inflate(R.layout.item_event, parent, false);
            holder = createEventHolder(resultView);
            resultView.setTag(holder);

            resultView.setTag(holder);
        } else {
            holder = (EventHolder) resultView.getTag();
        }

        SocialItem socialItem = (SocialItem) getItem(position);
        Event event = socialItem.getEvent();

        fillIcon(holder, event.getType());
        fillEventInfo(holder, event, null, null);
        fillEventClickAbility(holder.layoutRoot, holder.txtPlace, event, position);

        return resultView;
    }

    public View initSectionNameView(int position, View convertView, ViewGroup parent) {
        View resultView = convertView;
        HeaderHolder holder;

        if (resultView == null) {
            resultView = mInflater.inflate(R.layout.item_header, parent, false);

            holder = new HeaderHolder();
            holder.txtTitle = (TextView) resultView.findViewById(R.id.txtTitle);

            resultView.setTag(holder);
        } else {
            holder = (HeaderHolder) resultView.getTag();
        }

        HeaderItem item = (HeaderItem) getItem(position);
        holder.txtTitle.setText(item.getTitle());
        holder.txtTitle.setVisibility(View.VISIBLE);

        return resultView;
    }

    private void fillDate(EventHolder holder, Event event) {
        String fromTime = DateUtils.getInstance().getTime(mContext, event.getFromMillis());
        String toTime = DateUtils.getInstance().getTime(mContext, event.getToMillis());

        if (!TextUtils.isEmpty(fromTime) && !TextUtils.isEmpty(toTime)) {
            holder.txtFrom.setText(fromTime);
            holder.txtTo.setText(String.format(mContext.getString(R.string.to), toTime));
        } else {
            holder.txtFrom.setText(App.getContext().getString(R.string.twenty_four_hours));
            holder.txtFrom.setTextSize(TypedValue.COMPLEX_UNIT_PX, App.getContext().getResources().getDimension(R.dimen.text_size_micro));
            holder.txtTo.setText(App.getContext().getString(R.string.access));
        }

        holder.txtFrom.setVisibility(View.VISIBLE);
        holder.txtTo.setVisibility(View.VISIBLE);
    }

    private void fillIcon(EventHolder holder, long type) {
        if (Type.getIcon(type) != 0) {
            holder.icon.setVisibility(View.VISIBLE);
            holder.icon.setImageResource(Type.getIcon(type));
        } else {
            holder.icon.setVisibility(View.GONE);
        }
    }

    private void fillEventInfo(EventHolder holder, Event event, @Nullable String track, @Nullable List<String> speakerNameList) {
        holder.txtTitle.setText(event.getName());
        if (event.isFavorite()) {
            holder.txtTitle.setTextColor(mContext.getResources().getColor(R.color.link));
        } else {
            holder.txtTitle.setTextColor(mContext.getResources().getColor(R.color.black_100));
        }

        if (!TextUtils.isEmpty(event.getPlace())) {
            holder.txtPlace.setText(event.getPlace());
            holder.layoutPlace.setVisibility(View.VISIBLE);
        } else {
            holder.layoutPlace.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(track)) {
            holder.txtTrack.setText(track);
            holder.txtTrack.setVisibility(View.VISIBLE);
        } else {
            holder.txtTrack.setVisibility(View.GONE);
        }

        if (speakerNameList != null && !speakerNameList.isEmpty()) {
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < speakerNameList.size(); i++) {
                String name = speakerNameList.get(i);
                builder.append(name);

                if (i < speakerNameList.size() - 1) {
                    builder.append(mContext.getString(R.string.speaker_separator));
                }
            }

            holder.txtSpeakers.setText(builder.toString());
            holder.layoutSpeakers.setVisibility(View.VISIBLE);

        } else {
            holder.layoutSpeakers.setVisibility(View.GONE);
        }

        holder.expIcon.setImageResource(Level.getIcon(event.getExperienceLevel()));
    }

    private void fillDivider(EventHolder holder, boolean isFirst) {
        if (isFirst) {
            holder.divider.setVisibility(View.GONE);
            holder.marginDivider.setVisibility(View.VISIBLE);
        } else {
            holder.divider.setVisibility(View.VISIBLE);
            holder.marginDivider.setVisibility(View.GONE);
        }
    }

    private void fillFavorite(EventHolder holder) {
        if (mEventMode == DrawerManager.EventMode.Favorites) {
            holder.layoutTime.setBackgroundColor(Color.TRANSPARENT);
        } else {
            holder.layoutTime.setBackgroundColor(mContext.getResources().getColor(R.color.grey_400_trans));
        }
    }

    private void fillEventClickAbility(View layoutRoot, TextView txtPlace, Event event, final int position) {
        Context context = layoutRoot.getContext();
        layoutRoot.setBackgroundResource(R.drawable.selector_light);
        txtPlace.setMaxLines(SINGLE_LINE_COUNT);

        long eventType = event.getType();
        if (eventType == Type.FREE_SLOT || eventType == Type.COFFEBREAK || eventType == Type.LUNCH || eventType == Type.REGISTRATION) {
            layoutRoot.setBackgroundColor(context.getResources().getColor(R.color.black_20_trans));
            txtPlace.setMaxLines(MULTI_LINE_COUNT);
            layoutRoot.setClickable(false);
        } else {
            layoutRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(position);
                }
            });
        }
    }

    private static class HeaderHolder {
        TextView txtTitle;
    }

    private EventHolder createEventHolder(View resultView) {
        EventHolder holder = new EventHolder();
        holder.layoutRoot = (LinearLayout) resultView.findViewById(R.id.layoutRoot);
        holder.layoutTime = (LinearLayout) resultView.findViewById(R.id.timeLayout);
        holder.divider = resultView.findViewById(R.id.divider);
        holder.marginDivider = resultView.findViewById(R.id.margin_divider);
        holder.icon = (ImageView) resultView.findViewById(R.id.imgEventIcon);
        holder.expIcon = (ImageView) resultView.findViewById(R.id.imgExperience);
        holder.txtTitle = (TextView) resultView.findViewById(R.id.txtTitle);
        holder.txtFrom = (TextView) resultView.findViewById(R.id.txtFrom);
        holder.txtTo = (TextView) resultView.findViewById(R.id.txtTo);
        holder.layoutSpeakers = (LinearLayout) resultView.findViewById(R.id.layout_speakers);
        holder.layoutPlace = (LinearLayout) resultView.findViewById(R.id.layout_place);
        holder.txtSpeakers = (TextView) resultView.findViewById(R.id.txtSpeakers);
        holder.txtTrack = (TextView) resultView.findViewById(R.id.txtTrack);
        holder.txtPlace = (TextView) resultView.findViewById(R.id.txtPlace);
        return holder;
    }

    private static class EventHolder {

        LinearLayout layoutRoot;
        ImageView icon;
        ImageView expIcon;
        View divider;
        View marginDivider;
        LinearLayout layoutTime;
        LinearLayout layoutSpeakers;
        LinearLayout layoutPlace;
        TextView txtSpeakers;
        TextView txtTrack;
        TextView txtFrom;
        TextView txtTo;
        TextView txtTitle;
        TextView txtPlace;
    }
}
