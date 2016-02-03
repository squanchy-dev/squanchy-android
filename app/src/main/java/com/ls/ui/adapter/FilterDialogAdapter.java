package com.ls.ui.adapter;

import com.ls.drupalcon.R;
import com.ls.drupalcon.model.data.Level;
import com.ls.drupalcon.model.data.Track;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FilterDialogAdapter extends BaseExpandableListAdapter {

	private List<String> listDataHeader;
	private HashMap<String, String[]> listChildData;
	private List<Level> mLevelList;

	private List<Track> mTrackList;
	private List<List<Long>> mSelectedIds;

	private Listener mListener;
	private LayoutInflater mInflater;

	public interface Listener {
		 void onGroupClicked(int position);

		 void onChildClicked(int groupPosition, int ChildPosition);
	}

	public void setListener(Listener listener) {
		mListener = listener;
	}

	public FilterDialogAdapter(Context context, List<String> listDataHeader, HashMap<String, String[]> listChildData) {
		mInflater = LayoutInflater.from(context);
		this.listDataHeader = listDataHeader;
		this.listChildData = listChildData;

		mLevelList = new ArrayList<>();
		mTrackList = new ArrayList<>();

		mSelectedIds = new ArrayList<>();
		mSelectedIds.add(new ArrayList<Long>());
		mSelectedIds.add(new ArrayList<Long>());
	}

	@Override
	public String getGroup(int position) {
		return listDataHeader.get(position);
	}

	@Override
	public int getGroupCount() {
		return listDataHeader.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		String header = listDataHeader.get(groupPosition);
		return listChildData.get(header).length;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		String header = listDataHeader.get(groupPosition);
		return listChildData.get(header)[childPosititon];
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup viewGroup) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_filter_group, null);
		}

		final View divider = convertView.findViewById(R.id.divider);

		TextView txtHeader = (TextView) convertView.findViewById(R.id.txtTitle);
		txtHeader.setText(getGroup(groupPosition));
		txtHeader.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isExpanded) {
					divider.setVisibility(View.VISIBLE);
				} else {
					divider.setVisibility(View.INVISIBLE);
				}

				mListener.onGroupClicked(groupPosition);
			}
		});

		return convertView;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition, boolean isExpanded, View convertView, ViewGroup viewGroup) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_filter_list, null);
		}

		View divider = convertView.findViewById(R.id.dialogDivider);
		String childText;
		long childId;
		boolean displayDivider;

		if (groupPosition == 0) {
			childText = mLevelList.get(childPosition).getName();
			childId = mLevelList.get(childPosition).getId();
			displayDivider = childPosition == mLevelList.size() - 1;

		} else {
			childText = mTrackList.get(childPosition).getName();
			childId = mTrackList.get(childPosition).getId();
			displayDivider = childPosition == mTrackList.size() - 1;
		}

		if (displayDivider) {
			divider.setVisibility(View.VISIBLE);
		} else {
			divider.setVisibility(View.INVISIBLE);
		}

		TextView txtChild = (TextView) convertView.findViewById(R.id.txtItemTitle);
		txtChild.setText(childText);

		CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
		if (isSelected(childId, groupPosition)) {
			checkBox.setChecked(true);
		} else {
			checkBox.setChecked(false);
		}

		convertView.findViewById(R.id.layoutChild).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onChildClicked(groupPosition, childPosition);
				}
			}
		});

		return convertView;
	}

	@Override
	public boolean isChildSelectable(int i, int i2) {
		return true;
	}

	public void setData(List<Level> levelList, List<Track> trackList) {
		if (levelList != null && trackList != null) {
			mLevelList.addAll(levelList);
			mTrackList.addAll(trackList);
		}
	}

	public void setCheckedPositions(List<List<Long>> selectedIds) {
		mSelectedIds = selectedIds;
	}

	public void setClicked(int groupPosition, int childPosition) {
		long selectedId;
		if (groupPosition == 0) {
			selectedId = mLevelList.get(childPosition).getId();
			if (mSelectedIds.get(0).contains(selectedId)) {
				mSelectedIds.get(0).remove(selectedId);
			} else {
				mSelectedIds.get(0).add(selectedId);
			}

		} else {
			selectedId = mTrackList.get(childPosition).getId();
			if (mSelectedIds.get(1).contains(selectedId)) {
				mSelectedIds.get(1).remove(selectedId);
			} else {
				mSelectedIds.get(1).add(selectedId);
			}
		}
		notifyDataSetChanged();
	}

	private boolean isSelected(long id, int groupPosition) {
		if (!mSelectedIds.isEmpty()) {
			if (groupPosition == 0) {
				List<Long> ids = mSelectedIds.get(0);
				if (ids.contains(id)) {
					return true;
				}
			} else if (groupPosition == 1) {
				List<Long> ids = mSelectedIds.get(1);
				if (ids.contains(id)) {
					return true;
				}
			}
		}
		return false;
	}

	public List<List<Long>> getSelectedIds() {
		return mSelectedIds;
	}
}
