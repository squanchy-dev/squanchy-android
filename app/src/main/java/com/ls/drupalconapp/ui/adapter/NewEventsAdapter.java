package com.ls.drupalconapp.ui.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ls.drupalconapp.R;
import com.ls.drupalconapp.app.App;
import com.ls.drupalconapp.model.DatabaseManager;
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

import java.text.SimpleDateFormat;
import java.util.List;

public class NewEventsAdapter extends BaseAdapter {

	private Context mContext;
	private SimpleDateFormat mDateFormat = new SimpleDateFormat("dd/MM");
	private List<EventListItem> mData;
	private LayoutInflater mInflater;
	private DatabaseManager databaseManager;

    private DrawerManager.EventMode mEventMode;

    private View.OnClickListener favoriteAdditionAction;
	private OnClickListener mClickListener;

	public interface OnClickListener {
		public void onClick(int position);
	}


    public NewEventsAdapter(Context context, List<EventListItem> data, DrawerManager.EventMode mode, OnClickListener clickListener) {
		mContext = context;
        mInflater = LayoutInflater.from(context);
        mData = data;
        mEventMode = mode;
        databaseManager = DatabaseManager.instance();
		mClickListener = clickListener;
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
        } else if (itemViewType == EventListItem.TYPE_HEADER){
            resultView = initHeaderView(position, convertView, parent);
        } else {
            resultView = new View(mInflater.getContext());
        }

        return resultView;
    }

    public View initHeaderView(int position, View convertView, ViewGroup parent) {
        View resultView = convertView;
        HeaderHolder holder = null;

        if (resultView == null) {
            resultView = mInflater.inflate(R.layout.item_header, parent, false);
            holder = new HeaderHolder();
            holder.txtTitle = (TextView) resultView.findViewById(R.id.txtTitle);
            resultView.setTag(holder);
        } else {
            holder = (HeaderHolder) resultView.getTag();
        }

        final HeaderItem item = (HeaderItem) getItem(position);
        holder.txtTitle.setText(item.getTitle());
		holder.txtTitle.setVisibility(View.VISIBLE);

        return resultView;
    }

    public View initTimeRangeView(final int position, View convertView, ViewGroup parent) {
        View resultView = convertView;
        TimeRangeHolder holder = null;

        if (resultView == null) {
            resultView = mInflater.inflate(R.layout.item_event_generic, parent, false);

            holder = new TimeRangeHolder();
			holder.layoutRoot = (RelativeLayout) resultView.findViewById(R.id.layoutRoot);
            holder.divider = resultView.findViewById(R.id.divider);
            holder.marginDivider = resultView.findViewById(R.id.margin_divider);
            holder.icon = (ImageView) resultView.findViewById(R.id.imgEventIcon);
            holder.expIcon = (ImageView) resultView.findViewById(R.id.imgExperience);
            holder.txtTitle = (TextView) resultView.findViewById(R.id.txtTitle);
            holder.txtFrom = (TextView) resultView.findViewById(R.id.txtFrom);
            holder.txtTo = (TextView) resultView.findViewById(R.id.txtTo);
            holder.layoutSpeakers = (LinearLayout) resultView.findViewById(R.id.layout_speakers);
            holder.layoutTrack = (LinearLayout) resultView.findViewById(R.id.layout_track);
            holder.layoutPlace = (LinearLayout) resultView.findViewById(R.id.layout_place);
            holder.txtSpeakers = (TextView) resultView.findViewById(R.id.txtSpeakers);
            holder.txtTrack = (TextView) resultView.findViewById(R.id.txtTrack);
            holder.txtPlace = (TextView) resultView.findViewById(R.id.txtPlace);
            resultView.setTag(holder);
        } else {
            holder = (TimeRangeHolder) resultView.getTag();
        }

        final TimeRangeItem item = (TimeRangeItem) getItem(position);
        final Event event = item.getEvent();

        if (!item.getSpeakers().isEmpty()){
            item.getSpeakers().size();
        }

		String fromTime = item.getFromTime();
		String toTime = item.getToTime();

		if (android.text.format.DateFormat.is24HourFormat(mContext)) {
			if (fromTime != null && toTime != null) {
				fromTime = DateUtils.convertDateTo24Format(fromTime);
				toTime = DateUtils.convertDateTo24Format(toTime);
			}
		}

        if (fromTime != null && toTime != null) {
            holder.txtFrom.setText(fromTime);
            holder.txtTo.setText("to " + toTime);
        } else {
            holder.txtFrom.setText(App.getContext().getString(R.string.twenty_four_hours));
            holder.txtFrom.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    App.getContext().getResources().getDimension(R.dimen.text_size_micro));
            holder.txtTo.setText(App.getContext().getString(R.string.access));
        }

        holder.txtTitle.setText(event.getName());

        if (!event.getPlace().equals("")) {
            holder.txtPlace.setText(event.getPlace());
            holder.layoutPlace.setVisibility(View.VISIBLE);
        } else {
            holder.layoutPlace.setVisibility(View.GONE);
        }

        if (item.getTrack() != null){
            holder.txtTrack.setText(item.getTrack());
            holder.layoutTrack.setVisibility(View.VISIBLE);
        } else {
            holder.layoutTrack.setVisibility(View.GONE);
        }

        if (!item.getSpeakers().isEmpty()){

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

        if (Type.getIcon(item.getType()) != 0){
            holder.icon.setVisibility(View.VISIBLE);
        }
        holder.icon.setImageResource(Type.getIcon(item.getType()));

        holder.expIcon.setImageResource(Level.getIcon(event.getExperienceLevel()));

        if (item.isFirst()) {
            holder.divider.setVisibility(View.GONE);
            holder.marginDivider.setVisibility(View.VISIBLE);
        } else {
            holder.divider.setVisibility(View.VISIBLE);
            holder.marginDivider.setVisibility(View.GONE);
        }

		if (mEventMode == DrawerManager.EventMode.Favorites){
			resultView.findViewById(R.id.dark_background).setVisibility(View.GONE);
		}

		initEventClickAbility(holder.layoutRoot, holder.txtPlace, event, position);

        return resultView;
    }

    public View initProgramView(final int position, View convertView, ViewGroup parent) {
        View resultView = convertView;
        ProgramsHolder holder = null;

        if (resultView == null) {
            resultView = mInflater.inflate(R.layout.item_event_generic, parent, false);

            holder = new ProgramsHolder();
			holder.layoutRoot = (RelativeLayout) resultView.findViewById(R.id.layoutRoot);
            holder.divider = resultView.findViewById(R.id.divider);
            holder.marginDivider = resultView.findViewById(R.id.margin_divider);
            holder.expIcon = (ImageView) resultView.findViewById(R.id.imgExperience);
            holder.txtTitle = (TextView) resultView.findViewById(R.id.txtTitle);
            holder.layoutTime = (RelativeLayout) resultView.findViewById(R.id.timeLayout);
            holder.layoutSpeakers = (LinearLayout) resultView.findViewById(R.id.layout_speakers);
            holder.layoutTrack = (LinearLayout) resultView.findViewById(R.id.layout_track);
            holder.layoutPlace = (LinearLayout) resultView.findViewById(R.id.layout_place);
            holder.txtSpeakers = (TextView) resultView.findViewById(R.id.txtSpeakers);
            holder.txtTrack = (TextView) resultView.findViewById(R.id.txtTrack);
            holder.txtPlace = (TextView) resultView.findViewById(R.id.txtPlace);
            resultView.setTag(holder);
        } else {
            holder = (ProgramsHolder) resultView.getTag();
        }

        holder.layoutTime.setVisibility(View.INVISIBLE);

        final ProgramItem item = (ProgramItem) getItem(position);
        final Event event = item.getEvent();

		holder.txtTitle.setText(event.getName());

        if (!event.getPlace().equals("")) {
            holder.txtPlace.setText(event.getPlace());
            holder.layoutPlace.setVisibility(View.VISIBLE);
        } else {
            holder.layoutPlace.setVisibility(View.GONE);
        }

        if (item.getTrack() != null){
            holder.txtTrack.setText(item.getTrack());
            holder.layoutTrack.setVisibility(View.VISIBLE);
        } else {
            holder.layoutTrack.setVisibility(View.GONE);
        }

        if (!item.getSpeakers().isEmpty()){

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
				mClickListener.onClick(position);
			}
		});
		initEventClickAbility(holder.layoutRoot, holder.txtPlace, event, position);

        return resultView;
    }

    public View initBofsView(final int position, View convertView, ViewGroup parent) {
        View resultView = convertView;
        BofsHolder holder;

        if (resultView == null) {
            resultView = mInflater.inflate(R.layout.item_event_generic, parent, false);

            holder = new BofsHolder();
			holder.layoutRoot = (RelativeLayout) resultView.findViewById(R.id.layoutRoot);
            holder.divider = resultView.findViewById(R.id.divider);
            holder.marginDivider = resultView.findViewById(R.id.margin_divider);
            holder.txtTitle = (TextView) resultView.findViewById(R.id.txtTitle);
            holder.layoutTime = (RelativeLayout) resultView.findViewById(R.id.timeLayout);
            holder.layoutPlace = (LinearLayout) resultView.findViewById(R.id.layout_place);
            holder.txtPlace = (TextView) resultView.findViewById(R.id.txtPlace);

            resultView.setTag(holder);
        } else {
            holder = (BofsHolder) resultView.getTag();
        }

        final BofsItem item = (BofsItem) getItem(position);
        final Event event = item.getEvent();

		holder.layoutTime.setVisibility(View.INVISIBLE);
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

    private View initSocialView(final int position, View convertView, ViewGroup parent){
        View resultView = convertView;
        SocialsHolder holder = null;

        if (resultView == null){
            resultView = mInflater.inflate(R.layout.item_social_event, parent, false);
            holder = new SocialsHolder();
			holder.layoutRoot = (RelativeLayout) resultView.findViewById(R.id.layoutRoot);
            holder.layoutPlace = (LinearLayout) resultView.findViewById(R.id.layout_place);
            holder.txtFrom = (TextView) resultView.findViewById(R.id.txtFrom);
            holder.txtTo = (TextView) resultView.findViewById(R.id.txtTo);
            holder.txtTitle = (TextView) resultView.findViewById(R.id.txtTitle);
            holder.txtPlace = (TextView) resultView.findViewById(R.id.txtPlace);
            resultView.setTag(holder);
        } else {
            holder = (SocialsHolder) resultView.getTag();
        }

        final SocialItem item = (SocialItem) getItem(position);
        final Event event = item.getEvent();

		String fromTime = event.getFromTime();
		String toTime = event.getToTime();

		if (android.text.format.DateFormat.is24HourFormat(mContext)) {
			if (fromTime != null && toTime != null) {
				fromTime = DateUtils.convertDateTo24Format(fromTime);
				toTime = DateUtils.convertDateTo24Format(toTime);
			}
		}

//		holder.txtFrom.setText(fromTime);
//		holder.txtTo.setText("to " + toTime);
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
		layoutRoot.setBackgroundResource(R.drawable.selector_white_trans);
		txtPlace.setMaxLines(1);

		long eventType = event.getType();
		if (eventType == Type.FREE_SLOT || eventType == Type.COFFEBREAK || eventType == Type.LUNCH || eventType == Type.REGISTRATION) {
			layoutRoot.setBackgroundColor(context.getResources().getColor(R.color.gray_20_trans));
			txtPlace.setMaxLines(3);
			layoutRoot.setClickable(false);
		} else {
			layoutRoot.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mClickListener.onClick(position);
				}
			});
		}
	}

    private void initFavoriteButton(final ImageView btnFavorite, final Event event) {
        btnFavorite.setImageResource(event.isFavorite()
                ? R.drawable.selector_favorite_checked
                : R.drawable.selector_favorite_unchecked);

        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isFavorite = !event.isFavorite();
                btnFavorite.setImageResource(isFavorite
                        ? R.drawable.selector_favorite_checked
                        : R.drawable.selector_favorite_unchecked);

                event.setFavorite(isFavorite);

                if (favoriteAdditionAction != null) {
                    favoriteAdditionAction.onClick(v);
                }
            }
        });
    }

    public void setData(List<EventListItem> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void addData(List<EventListItem> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void updateEvent(long eventId, boolean isFavorite) {
        for (EventListItem eventListItem : mData) {
            Event event = eventListItem.getEvent();
            if (event != null && event.getId() == eventId) {
                event.setFavorite(isFavorite);
                notifyDataSetChanged();
                return;
            }
        }
    }

    public void setFavoriteAdditionAction(View.OnClickListener favoriteAdditionAction) {
        this.favoriteAdditionAction = favoriteAdditionAction;
    }

    private static class HeaderHolder {
        TextView txtTitle;
    }

    private static class TimeRangeHolder {

		RelativeLayout layoutRoot;
        ImageView icon;
        ImageView expIcon;
        View divider;
        View marginDivider;
        LinearLayout layoutTrack;
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

		RelativeLayout layoutRoot;
        RelativeLayout layoutTime;
        LinearLayout layoutPlace;
        TextView txtTitle;
        TextView txtPlace;
        View divider;
        View marginDivider;
    }


    private static class ProgramsHolder {

		RelativeLayout layoutRoot;
        ImageView expIcon;
        RelativeLayout layoutTime;
        LinearLayout layoutTrack;
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

		RelativeLayout layoutRoot;
        LinearLayout layoutPlace;
        TextView txtFrom;
        TextView txtTo;
        TextView txtTitle;
        TextView txtPlace;
		View divider;
    }
}
