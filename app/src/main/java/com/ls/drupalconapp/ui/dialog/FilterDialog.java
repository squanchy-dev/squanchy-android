package com.ls.drupalconapp.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.ls.drupalconapp.R;
import com.ls.drupalconapp.model.PreferencesManager;
import com.ls.drupalconapp.model.data.Level;
import com.ls.drupalconapp.model.data.Track;
import com.ls.drupalconapp.ui.adapter.FilterDialogAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FilterDialog extends DialogFragment {

	private static final String ARG_TRACKS = "ARG_TRACKS";
	private static final String ARG_EXP_LEVEL = "ARG_EXP_LEVEL";

	private static List<Level> mLevelList;
	private static List<Track> mTrackList;
	private static List<List<Long>> mSelectedIds;

    private OnCheckedPositionsPass mListener;
	private FilterDialogAdapter mAdapter;

	public interface OnCheckedPositionsPass {
		void onNewFilterApplied();
	}

	public static FilterDialog newInstance(String[] tracks, String[] expLevels) {
		FilterDialog filterDialog = new FilterDialog();

		Bundle args = new Bundle();
		args.putStringArray(ARG_TRACKS, tracks);
		args.putStringArray(ARG_EXP_LEVEL, expLevels);
		filterDialog.setArguments(args);

		mLevelList = new ArrayList<>();
		mTrackList = new ArrayList<>();
		mSelectedIds = new ArrayList<>();

		return filterDialog;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mListener = (OnCheckedPositionsPass) activity;
	}

	public void setData(List<Level> levelList, List<Track> trackList) {
		if (levelList != null && trackList != null) {
			mLevelList.addAll(levelList);
			mTrackList.addAll(trackList);
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		//getArguments
		String[] tracks = getArguments().getStringArray(ARG_TRACKS);
		String[] expLevels = getArguments().getStringArray(ARG_EXP_LEVEL);
		if (tracks == null || expLevels == null) {
			throw new IllegalArgumentException("Tracks or Experience Levels is null! Please use newInstance() method and init them");
		}

		LayoutInflater inflater = getActivity().getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.dialog_filter, null);

		final ExpandableListView listView = (ExpandableListView) dialogView.findViewById(R.id.listView);

		DisplayMetrics metrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int width = metrics.widthPixels;

		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
			listView.setIndicatorBounds(width - (int) getResources().getDimension(R.dimen.exp_list_indicator_bounds_left),
					width - (int) getResources().getDimension(R.dimen.exp_list_indicator_bounds_right));
		} else {
			listView.setIndicatorBoundsRelative(width - (int) getResources().getDimension(R.dimen.exp_list_indicator_bounds_left),
					width - (int) getResources().getDimension(R.dimen.exp_list_indicator_bounds_right));
		}

		List<String> listDataHeader = new ArrayList<String>();
		listDataHeader.add(getActivity().getString(R.string.exp_levels));
		listDataHeader.add(getActivity().getString(R.string.tracks));

		HashMap<String, String[]> listDataChild = new HashMap<>();
		listDataChild.put(listDataHeader.get(0), expLevels);
		listDataChild.put(listDataHeader.get(1), tracks);

		mSelectedIds = loadSelectedIds();
		mAdapter = new FilterDialogAdapter(getActivity(), listDataHeader, listDataChild);
		mAdapter.setData(mLevelList, mTrackList);
		mAdapter.setCheckedPositions(mSelectedIds);
		mAdapter.setListener(new FilterDialogAdapter.Listener() {
			@Override
			public void onGroupClicked(int groupPosition) {
				if (listView.isGroupExpanded(groupPosition)) {
					listView.collapseGroup(groupPosition);
				} else {
					listView.expandGroup(groupPosition);
				}
			}

			@Override
			public void onChildClicked(int groupPosition, int childPosition) {
				mAdapter.setClicked(groupPosition, childPosition);
            }
        });

		listView.setAdapter(mAdapter);

        for (int i = 0; i < mSelectedIds.size(); i++) {
            List<Long> ids = mSelectedIds.get(i);
            if (!ids.isEmpty()) {
                listView.expandGroup(i);
            }
        }

		TextView apply = (TextView) dialogView.findViewById(R.id.btnApply);
		apply.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				applyFilter();
				dismissAllowingStateLoss();
			}
		});

		TextView clear = (TextView) dialogView.findViewById(R.id.btnClear);
		clear.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				clearFilter();
				dismissAllowingStateLoss();
			}
		});

		builder.setView(dialogView);
		Dialog result = builder.create();
		result.requestWindowFeature(Window.FEATURE_NO_TITLE);

		return result;
	}

	private void applyFilter() {
		saveSelectedItems(mAdapter.getSelectedIds());
		if (mListener != null) {
			mListener.onNewFilterApplied();
		}
	}

	public void clearFilter() {
        clearSelectedItems();
        saveSelectedItems(mSelectedIds);
        if (mListener != null) {
            mListener.onNewFilterApplied();
        }
    }

	private void clearSelectedItems() {
		if (mSelectedIds != null && !mSelectedIds.isEmpty()) {
			mSelectedIds.get(0).clear();
			mSelectedIds.get(1).clear();
		}
	}

	private void saveSelectedItems(List<List<Long>> selectedIds) {
		if (selectedIds != null && !selectedIds.isEmpty()) {
			PreferencesManager.getInstance().saveExpLevel(selectedIds.get(0));
			PreferencesManager.getInstance().saveTrack(selectedIds.get(1));
		}
	}

	@NonNull
	private List<List<Long>> loadSelectedIds() {
		List<List<Long>> selectedIds = new ArrayList<>();
		selectedIds.add(PreferencesManager.getInstance().loadExpLevel());
		selectedIds.add(PreferencesManager.getInstance().loadTracks());
		return selectedIds;
	}
}
