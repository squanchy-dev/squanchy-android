package com.ls.drupalconapp.ui.adapter;

import com.ls.drupalconapp.R;
import com.ls.drupalconapp.model.vo.AbstractEvent;
import com.ls.drupalconapp.model.vo.BreakEvent;
import com.ls.drupalconapp.model.vo.ProgramEvent;
import com.ls.drupalconapp.ui.listener.OnEventClickListener;
import com.ls.utils.UIUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class EventsAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<AbstractEvent> mEvents;
    private OnEventClickListener mOnEventClickListener;

    public EventsAdapter(Context context, List<AbstractEvent> events, OnEventClickListener listener) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mEvents = events;
        mOnEventClickListener = listener;
    }

    @Override
    public int getCount() {
        return mEvents.size();
    }

    @Override
    public AbstractEvent getItem(int position) {
        return mEvents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mEvents.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;

        AbstractEvent absEvent = mEvents.get(position);
        if(absEvent == null) {
            return mInflater.inflate(R.layout.item_program, null);
        }

        if(!absEvent.isBreakEvent()) {
            ProgramEvent programEvent = (ProgramEvent) absEvent;
            if(programEvent.getDate() == null || programEvent.getBeginTime() == null
                    || programEvent.getEndTime() == null) {
                return mInflater.inflate(R.layout.item_program, null);
            }
        }

        row = initView(position, absEvent);

        return row;
    }

    private View initView(int position, AbstractEvent absEvent) {
        if(absEvent.isBreakEvent()) {
            return initBreakEventLayout((BreakEvent)absEvent);
        }

        ProgramEvent event = (ProgramEvent)absEvent;

        if(position == 0) {
            return initLayoutWithDate(position, event);
        }

        AbstractEvent absPrevEvent = mEvents.get(position - 1);
        if(absPrevEvent.isBreakEvent()) {
            return initLayoutWithDate(position, event);
        }

        ProgramEvent prevEvent = (ProgramEvent)absPrevEvent;
        if(prevEvent.getDate() == null || prevEvent.getBeginTime() == null
                || prevEvent.getEndTime() == null) {
            return initLayoutWithDate(position, event);
        }

        if(event.getDate().equals(prevEvent.getDate()) && event.getBeginTime().equals(prevEvent.getBeginTime()) &&
                event.getEndTime().equals(prevEvent.getEndTime())) {
            return initSimpleEventLayout(position, event);
        } else {
            return initLayoutWithDate(position, event);
        }
    }

    private View initBreakEventLayout(BreakEvent breakEvent) {
        View row = mInflater.inflate(R.layout.item_event_generic, null);

        ImageView imgIcon = (ImageView) row.findViewById(R.id.imgEventIcon);
        imgIcon.setImageResource(breakEvent.isLanchBreak() ? R.drawable.ic_lanch_break : R.drawable.ic_coffee_break);

//        fillTime((TextView)row.findViewById(R.id.txtTime), breakEvent.getBeginTime(), breakEvent.getEndTime());

        return row;
    }

    private View initSimpleEventLayout(int position, ProgramEvent event) {
        View row = mInflater.inflate(R.layout.item_program_event, null);
        EventHolder holder = new EventHolder();

        initEventHolder(row, holder);

        fillEventInfo(position, holder, event);

        return row;
    }

    private void fillTime(EventDateHolder holder, String date, String beginTime, String endTime, boolean isKeynote) {
        holder.txtTime.setText(beginTime);
        UIUtils.addSizeSupersciptSpanToTextView(holder.txtTime, " " + mContext.getString(R.string.to), 0.65f);
        holder.txtTime.append("\n" + endTime);

        holder.txtDate.setText(date);
        holder.txtDate.setVisibility(View.VISIBLE);

//        holder.icon.setImageResource(isKeynote ?  R.drawable.ic_keynote : R.drawable.ic_clock);
    }

    private void fillTime(TextView txtTime, String beginTime, String endTime) {
        txtTime.setText(beginTime);
        UIUtils.addSizeSupersciptSpanToTextView(txtTime, " " + mContext.getString(R.string.to), 0.65f);
        txtTime.append("\n" + endTime);
    }

    private View initLayoutWithDate(int position, ProgramEvent event) {
        View row = mInflater.inflate(R.layout.item_program, null);
        EventDateHolder dateHolder = new EventDateHolder();

        initEventHolder(row, dateHolder);
        dateHolder.icon = (ImageView) row.findViewById(R.id.imgEventIcon);
//        dateHolder.txtDate = (TextView) row.findViewById(R.id.txtDate);
//        dateHolder.txtTime = (TextView) row.findViewById(R.id.txtTime);

        //fill views
        fillTime(dateHolder, event.getDate(), event.getBeginTime(), event.getEndTime(), event.isKeynote());
        fillEventInfo(position, dateHolder, event);

        return row;
    }

    private void fillEventInfo(final int position, final EventHolder dateHolder, final ProgramEvent event) {
        dateHolder.txtReportName.setText(event.getName());

        dateHolder.txtSpeakerName.setText(event.getSpeakersName());
        dateHolder.txtTrack.setText(event.getTrack());
        dateHolder.txtExpLevel.setText(event.getExpLevel());

        dateHolder.btnAddToSchedule.setImageResource(event.isAddToSchedule() ? R.drawable.selector_favorite_checked :
                R.drawable.selector_favorite_unchecked);
        dateHolder.btnAddToSchedule.setOnClickListener(new View.OnClickListener() {
            boolean isAdded = event.isAddToSchedule();

            @Override
            public void onClick(View v) {
                dateHolder.btnAddToSchedule.setImageResource(isAdded ? R.drawable.selector_favorite_unchecked :
                        R.drawable.selector_favorite_checked);
                isAdded = !isAdded;
            }
        });

        dateHolder.holderEventDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnEventClickListener.onEventClicked(position);
            }
        });
    }

    private void initEventHolder(View row, EventHolder holder) {
        holder.btnAddToSchedule = (ImageView)row.findViewById(R.id.btnFavorite);
        holder.txtReportName = (TextView)row.findViewById(R.id.txtName);
        holder.txtSpeakerName = (TextView) row.findViewById(R.id.txtSpeaker);
        holder.txtExpLevel = (TextView) row.findViewById(R.id.txtExpLevel);
        holder.txtTrack = (TextView) row.findViewById(R.id.txtTrack);
        holder.holderEventDetails = (RelativeLayout) row.findViewById(R.id.holderEventDetails);
    }

    private class EventHolder {
        ImageView btnAddToSchedule;
        TextView txtReportName;
        TextView txtSpeakerName;
        TextView txtTrack;
        TextView txtExpLevel;
        RelativeLayout holderEventDetails;
    }

    private class EventDateHolder extends EventHolder {
        ImageView icon;
        TextView txtTime;
        TextView txtDate;
    }
}
