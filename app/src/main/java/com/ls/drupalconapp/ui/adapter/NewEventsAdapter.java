package com.ls.drupalconapp.ui.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ls.drupalconapp.R;
import com.ls.drupalconapp.app.App;
import com.ls.drupalconapp.model.data.Event;
import com.ls.drupalconapp.model.data.Level;
import com.ls.drupalconapp.model.data.Type;
import com.ls.drupalconapp.ui.adapter.item.BofsItem;
import com.ls.drupalconapp.ui.adapter.item.EventListItem;
import com.ls.drupalconapp.ui.adapter.item.HeaderItem;
import com.ls.drupalconapp.ui.adapter.item.ProgramItem;
import com.ls.drupalconapp.ui.adapter.item.SocialItem;
import com.ls.drupalconapp.ui.adapter.item.TimeRangeItem;
import com.ls.drupalconapp.ui.drawer.DrawerManager;
import com.ls.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class NewEventsAdapter extends BaseAdapter {

    private Context mContext;
    private List<EventListItem> mData;
    private LayoutInflater mInflater;

    private DrawerManager.EventMode mEventMode;
    private Listener mListener;

    public interface Listener {
        void onClick(int position);
    }

    public NewEventsAdapter(Context context) {
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
        return 5;
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

    public void setOnClickListener(Listener listener) {
        mListener = listener;
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
        } else if (itemViewType == EventListItem.TYPE_HEADER) {
            resultView = initHeaderView(position, convertView, parent);
        } else {
            resultView = new View(mInflater.getContext());
        }

        return resultView;
    }

    public View initHeaderView(int position, View convertView, ViewGroup parent) {
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

    public View initTimeRangeView(final int position, View convertView, ViewGroup parent) {
        View resultView = convertView;
        TimeRangeHolder holder;

        if (resultView == null) {
            resultView = mInflater.inflate(R.layout.item_event, parent, false);
            holder = new TimeRangeHolder();
            holder.layoutRoot = (LinearLayout) resultView.findViewById(R.id.layoutRoot);
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
            resultView.setTag(holder);
        } else {
            holder = (TimeRangeHolder) resultView.getTag();
        }

        TimeRangeItem timeRange = (TimeRangeItem) getItem(position);
        Event event = timeRange.getEvent();
        String fromTime = timeRange.getFromTime();
        String toTime = timeRange.getToTime();


        if (android.text.format.DateFormat.is24HourFormat(mContext)) {
            if (fromTime != null && toTime != null) {
                fromTime = DateUtils.convertDateTo24Format(fromTime);
                toTime = DateUtils.convertDateTo24Format(toTime);
            }
        }

        if (Type.getIcon(timeRange.getType()) != 0) {
            holder.icon.setVisibility(View.VISIBLE);
            holder.icon.setImageResource(Type.getIcon(timeRange.getType()));
        } else {
            holder.icon.setVisibility(View.GONE);
        }

        if (fromTime != null && toTime != null) {
            holder.txtFrom.setText(fromTime);
            holder.txtTo.setText("to " + toTime);
        } else {
            holder.txtFrom.setText(App.getContext().getString(R.string.twenty_four_hours));
            holder.txtFrom.setTextSize(TypedValue.COMPLEX_UNIT_PX, App.getContext().getResources().getDimension(R.dimen.text_size_micro));
            holder.txtTo.setText(App.getContext().getString(R.string.access));
        }

        holder.txtTitle.setText(event.getName());
        if (!event.getPlace().equals("")) {
            holder.txtPlace.setText(event.getPlace());
            holder.layoutPlace.setVisibility(View.VISIBLE);
        } else {
            holder.layoutPlace.setVisibility(View.GONE);
        }

        if (timeRange.getTrack() != null) {
            holder.txtTrack.setText(timeRange.getTrack());
            holder.txtTrack.setVisibility(View.VISIBLE);
        } else {
            holder.txtTrack.setVisibility(View.GONE);
        }

        if (!timeRange.getSpeakers().isEmpty()) {
            List<String> speakers = timeRange.getSpeakers();
            StringBuilder builder = new StringBuilder(speakers.get(0));

            if (speakers.size() > 1) {
                builder.append(", ");
                builder.append(speakers.get(1));
            }
            holder.txtSpeakers.setText(builder.toString());
            holder.layoutSpeakers.setVisibility(View.VISIBLE);
        } else {
            holder.layoutSpeakers.setVisibility(View.GONE);
        }

        if (timeRange.isFirst()) {
            holder.divider.setVisibility(View.GONE);
            holder.marginDivider.setVisibility(View.VISIBLE);
        } else {
            holder.divider.setVisibility(View.VISIBLE);
            holder.marginDivider.setVisibility(View.GONE);
        }

        if (mEventMode == DrawerManager.EventMode.Favorites) {
            resultView.findViewById(R.id.timeLayout).setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));
        }

        holder.expIcon.setImageResource(Level.getIcon(event.getExperienceLevel()));
        initEventClickAbility(holder.layoutRoot, holder.txtPlace, event, position);

        return resultView;
    }

    public View initProgramView(final int position, View convertView, ViewGroup parent) {
        View resultView = convertView;
        ProgramsHolder holder;

        if (resultView == null) {
            resultView = mInflater.inflate(R.layout.item_event, parent, false);

            holder = new ProgramsHolder();
            holder.layoutRoot = (LinearLayout) resultView.findViewById(R.id.layoutRoot);
            holder.divider = resultView.findViewById(R.id.divider);
            holder.marginDivider = resultView.findViewById(R.id.margin_divider);
            holder.expIcon = (ImageView) resultView.findViewById(R.id.imgExperience);
            holder.txtTitle = (TextView) resultView.findViewById(R.id.txtTitle);
            holder.layoutTime = (LinearLayout) resultView.findViewById(R.id.timeLayout);
            holder.layoutSpeakers = (LinearLayout) resultView.findViewById(R.id.layout_speakers);
            holder.layoutPlace = (LinearLayout) resultView.findViewById(R.id.layout_place);
            holder.txtSpeakers = (TextView) resultView.findViewById(R.id.txtSpeakers);
            holder.txtTrack = (TextView) resultView.findViewById(R.id.txtTrack);
            holder.txtPlace = (TextView) resultView.findViewById(R.id.txtPlace);
            resultView.setTag(holder);
        } else {
            holder = (ProgramsHolder) resultView.getTag();
        }

        ProgramItem item = (ProgramItem) getItem(position);
        Event event = item.getEvent();
        holder.txtTitle.setText(event.getName());

        if (!event.getPlace().equals("")) {
            holder.txtPlace.setText(event.getPlace());
            holder.layoutPlace.setVisibility(View.VISIBLE);
        } else {
            holder.layoutPlace.setVisibility(View.GONE);
        }

        if (item.getTrack() != null) {
            holder.txtTrack.setText(item.getTrack());
            holder.txtTrack.setVisibility(View.VISIBLE);
        } else {
            holder.txtTrack.setVisibility(View.GONE);
        }

        if (!item.getSpeakers().isEmpty()) {
            List<String> speakers = item.getSpeakers();
            StringBuilder builder = new StringBuilder(speakers.get(0));

            if (speakers.size() > 1) {
                builder.append(", ");
                builder.append(speakers.get(1));
            }
            holder.txtSpeakers.setText(builder.toString());
            holder.layoutSpeakers.setVisibility(View.VISIBLE);
        } else {
            holder.layoutSpeakers.setVisibility(View.GONE);
        }

        if (!item.isLast()) {
            holder.divider.setVisibility(View.GONE);
            holder.marginDivider.setVisibility(View.VISIBLE);
        } else {
            holder.divider.setVisibility(View.VISIBLE);
            holder.marginDivider.setVisibility(View.GONE);
        }
        holder.expIcon.setImageResource(Level.getIcon(event.getExperienceLevel()));
        holder.layoutRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(position);
            }
        });
        initEventClickAbility(holder.layoutRoot, holder.txtPlace, event, position);

        return resultView;
    }

    public View initBofsView(final int position, View convertView, ViewGroup parent) {
        View resultView = convertView;
        BofsHolder holder;

        if (resultView == null) {
            resultView = mInflater.inflate(R.layout.item_event, parent, false);

            holder = new BofsHolder();
            holder.layoutRoot = (LinearLayout) resultView.findViewById(R.id.layoutRoot);
            holder.divider = resultView.findViewById(R.id.divider);
            holder.marginDivider = resultView.findViewById(R.id.margin_divider);
            holder.txtTitle = (TextView) resultView.findViewById(R.id.txtTitle);
            holder.layoutTime = (LinearLayout) resultView.findViewById(R.id.timeLayout);
            holder.layoutPlace = (LinearLayout) resultView.findViewById(R.id.layout_place);
            holder.txtPlace = (TextView) resultView.findViewById(R.id.txtPlace);

            resultView.setTag(holder);
        } else {
            holder = (BofsHolder) resultView.getTag();
        }

        BofsItem item = (BofsItem) getItem(position);
        Event event = item.getEvent();
        holder.txtTitle.setText(event.getName());

        if (!event.getPlace().equals("")) {
            holder.txtPlace.setText(event.getPlace());
            holder.layoutPlace.setVisibility(View.VISIBLE);
        } else {
            holder.layoutPlace.setVisibility(View.GONE);
        }

        if (!item.isLast()) {
            holder.divider.setVisibility(View.GONE);
            holder.marginDivider.setVisibility(View.VISIBLE);
        } else {
            holder.divider.setVisibility(View.VISIBLE);
            holder.marginDivider.setVisibility(View.GONE);
        }
        initEventClickAbility(holder.layoutRoot, holder.txtPlace, event, position);

        return resultView;
    }

    private View initSocialView(final int position, View convertView, ViewGroup parent) {
        View resultView = convertView;
        SocialsHolder holder;

        if (resultView == null) {
            resultView = mInflater.inflate(R.layout.item_event, parent, false);
            holder = new SocialsHolder();
            holder.layoutRoot = (LinearLayout) resultView.findViewById(R.id.layoutRoot);
            holder.layoutPlace = (LinearLayout) resultView.findViewById(R.id.layout_place);
            holder.txtFrom = (TextView) resultView.findViewById(R.id.txtFrom);
            holder.txtTo = (TextView) resultView.findViewById(R.id.txtTo);
            holder.txtTitle = (TextView) resultView.findViewById(R.id.txtTitle);
            holder.txtPlace = (TextView) resultView.findViewById(R.id.txtPlace);
            resultView.setTag(holder);
        } else {
            holder = (SocialsHolder) resultView.getTag();
        }

        SocialItem item = (SocialItem) getItem(position);
        Event event = item.getEvent();
        holder.txtTitle.setText(event.getName());

        if (!event.getPlace().equals("")) {
            holder.txtPlace.setText(event.getPlace());
            holder.layoutPlace.setVisibility(View.VISIBLE);
        } else {
            holder.layoutPlace.setVisibility(View.GONE);
        }

        initEventClickAbility(holder.layoutRoot, holder.txtPlace, event, position);

        return resultView;
    }

    private void initEventClickAbility(View layoutRoot, TextView txtPlace, Event event, final int position) {
        Context context = layoutRoot.getContext();
        layoutRoot.setBackgroundResource(R.drawable.selector_light);
        txtPlace.setMaxLines(1);

        long eventType = event.getType();
        if (eventType == Type.FREE_SLOT || eventType == Type.COFFEBREAK || eventType == Type.LUNCH || eventType == Type.REGISTRATION) {
            layoutRoot.setBackgroundColor(context.getResources().getColor(R.color.black_20_trans));
            txtPlace.setMaxLines(3);
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

    private static class TimeRangeHolder {

        LinearLayout layoutRoot;
        ImageView icon;
        ImageView expIcon;
        View divider;
        View marginDivider;
        LinearLayout layoutSpeakers;
        LinearLayout layoutPlace;
        TextView txtSpeakers;
        TextView txtTrack;
        TextView txtFrom;
        TextView txtTo;
        TextView txtTitle;
        TextView txtPlace;
    }

    private static class BofsHolder {

        LinearLayout layoutRoot;
        LinearLayout layoutTime;
        LinearLayout layoutPlace;
        TextView txtTitle;
        TextView txtPlace;
        View divider;
        View marginDivider;
    }


    private static class ProgramsHolder {

        LinearLayout layoutRoot;
        ImageView expIcon;
        LinearLayout layoutTime;
        LinearLayout layoutSpeakers;
        LinearLayout layoutPlace;
        TextView txtSpeakers;
        TextView txtTrack;
        TextView txtTitle;
        TextView txtPlace;
        View divider;
        View marginDivider;

    }

    private static class SocialsHolder {

        LinearLayout layoutRoot;
        LinearLayout layoutPlace;
        TextView txtFrom;
        TextView txtTo;
        TextView txtTitle;
        TextView txtPlace;
    }
}
