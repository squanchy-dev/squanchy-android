package com.ls.drupalconapp.ui.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ls.drupalconapp.R;
import com.ls.drupalconapp.model.data.Level;
import com.ls.drupalconapp.model.data.Track;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FilterDialogAdapter extends BaseExpandableListAdapter {

	private LayoutInflater mInflater;
	private List<String> listDataHeader;
	private HashMap<String, String[]> listChildData;
	private SparseArray<SparseBooleanArray> checkedPositions;

	private List<Level> mLevelList;
	private List<Track> mTrackList;
	private List<List<Long>> mSelectedIds;

	private Listener mListener;

	public interface Listener {
		public void onGroupClicked(int position);

		public void onChildClicked(int groupPosition, int ChildPosition);
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

		checkedPositions = new SparseArray<SparseBooleanArray>();
	}

	public void setData(List<Level> levelList, List<Track> trackList) {
		if (levelList != null && trackList != null) {
			mLevelList.addAll(levelList);
			mTrackList.addAll(trackList);
		}
	}

	public void setCheckedPositions(List<List<Long>> selectedids) {
		mSelectedIds = selectedids;
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
	public Object getGroup(int i) {
		return listDataHeader.get(i);
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
	public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup viewGroup) {
		String headerTitle = (String) getGroup(groupPosition);

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_filter_group, null);
		}

		TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
		lblListHeader.setText(headerTitle);

		final CheckBox checkBoxGroup = (CheckBox) convertView.findViewById(R.id.checkBoxGroup);
		convertView.findViewById(R.id.layoutGroup).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onGroupClicked(groupPosition);
					checkBoxGroup.setChecked(!checkBoxGroup.isChecked());
				}
			}
		});

		View divider = convertView.findViewById(R.id.divider);

		if (groupPosition == 0) {
			divider.setVisibility(View.INVISIBLE);
		} else {
			divider.setVisibility(View.VISIBLE);
		}

		return convertView;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition, boolean isExpanded, View convertView, ViewGroup viewGroup) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_filter_list, null);
		}

		String childText;
		long childId;
		if (groupPosition == 0) {
			childText = mLevelList.get(childPosition).getName();
			childId = mLevelList.get(childPosition).getId();
		} else {
			childText = mTrackList.get(childPosition).getName();
			childId = mTrackList.get(childPosition).getId();
		}

		TextView txtListChild = (TextView) convertView.findViewById(R.id.txtItemTitle);
		txtListChild.setText(childText);

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
		if (mSelectedIds != null) {
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
