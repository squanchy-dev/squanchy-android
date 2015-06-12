package com.ls.drupalconapp.ui.adapter;

import com.ls.drupalconapp.R;
import com.ls.drupalconapp.model.vo.BoFsEvent;
import com.ls.drupalconapp.ui.listener.OnEventClickListener;
import com.ls.utils.UIUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class BoFsAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<BoFsEvent> mBoFsEvents;
    private OnEventClickListener mListener;

    public BoFsAdapter(Context context, List<BoFsEvent> boFsEvents, OnEventClickListener listener) {
        mInflater = LayoutInflater.from(context);
        mBoFsEvents = boFsEvents;
        mContext = context;
        mListener = listener;
    }

    @Override
    public int getCount() {
        return mBoFsEvents.size();
    }

    @Override
    public BoFsEvent getItem(int position) {
        return mBoFsEvents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mBoFsEvents.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;

        BoFsEvent event = mBoFsEvents.get(position);
        if(event == null) {
            return mInflater.inflate(R.layout.item_bofs, null);
        }

        row = initView(position, event);

        return row;
    }

    private View initView(int position, BoFsEvent event) {
        if(position == 0) {
            return initLayoutWithDate(position, event);
        }

        BoFsEvent prevEvent = mBoFsEvents.get(position - 1);
        if( prevEvent.getBeginTime() == null || prevEvent.getEndTime() == null) {
            return initLayoutWithDate(position, event);
        }

        if(event.getBeginTime().equals(prevEvent.getBeginTime()) && event.getEndTime().equals(prevEvent.getEndTime())) {
            return initSimpleEventLayout(position, event);
        } else {
            return initLayoutWithDate(position, event);
        }
    }

    private View initSimpleEventLayout(int position, BoFsEvent event) {
        View row = mInflater.inflate(R.layout.item_bofs_event, null);
        EventHolder holder = new EventHolder();

        initEventHolder(row, holder);

        fillEventInfo(position, holder, event);

        return row;
    }

    private void fillTime(EventDateHolder holder, String date, String beginTime, String endTime) {
        holder.txtTime.setText(beginTime);
        UIUtils.addSizeSupersciptSpanToTextView(holder.txtTime, " " + mContext.getString(R.string.to), 0.65f);
        holder.txtTime.append("\n" + endTime);

        holder.txtDate.setText(date);
        holder.txtDate.setVisibility(View.VISIBLE);
    }

    private View initLayoutWithDate(int position, BoFsEvent event) {
        View row = mInflater.inflate(R.layout.item_bofs, null);
        EventDateHolder dateHolder = new EventDateHolder();

        initEventHolder(row, dateHolder);
        dateHolder.icon = (ImageView) row.findViewById(R.id.imgEventIcon);
//        dateHolder.txtDate = (TextView) row.findViewById(R.id.txtDate);
//        dateHolder.txtTime = (TextView) row.findViewById(R.id.txtTime);

        //fill views
        fillTime(dateHolder, event.getDate(), event.getBeginTime(), event.getEndTime());
        fillEventInfo(position, dateHolder, event);

        return row;
    }

    private void fillEventInfo(final int position, final EventHolder dateHolder, final BoFsEvent event) {
        dateHolder.txtEventName.setText(event.getName());

        dateHolder.txtRoom.setText(event.getRoom());

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
                mListener.onEventClicked(position);
            }
        });
    }

    private void initEventHolder(View row, EventHolder holder) {
        holder.btnAddToSchedule = (ImageView)row.findViewById(R.id.btnFavorite);
        holder.txtEventName = (TextView)row.findViewById(R.id.txtName);
        holder.txtRoom = (TextView) row.findViewById(R.id.txtRoom);
        holder.holderEventDetails = (LinearLayout) row.findViewById(R.id.holderBoFsEventDetails);
    }

    private class EventHolder {
        ImageView btnAddToSchedule;
        TextView txtEventName;
        TextView txtRoom;
        LinearLayout holderEventDetails;
    }

    private class EventDateHolder extends EventHolder {
        ImageView icon;
        TextView txtTime;
        TextView txtDate;
    }
}
