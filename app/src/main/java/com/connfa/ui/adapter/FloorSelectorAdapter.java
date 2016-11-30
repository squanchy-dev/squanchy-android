package com.connfa.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.connfa.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 14.06.2016.
 */
public class FloorSelectorAdapter extends BaseAdapter implements ThemedSpinnerAdapter {
    private final ThemedSpinnerAdapter.Helper mDropDownHelper;
    private final LayoutInflater mInflater;
    private List<String> mNames;

    public FloorSelectorAdapter(Context context, List<String> names) {
        mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);
        mInflater = LayoutInflater.from(context);
        mNames = new ArrayList<>(names);
    }

    @Override
    public void setDropDownViewTheme(Resources.Theme theme) {
        mDropDownHelper.setDropDownViewTheme(theme);
    }

    @Override
    public Resources.Theme getDropDownViewTheme() {
        return mDropDownHelper.getDropDownViewTheme();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            // Inflate the drop down using the helper's LayoutInflater
            LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
            convertView = inflater.inflate(R.layout.fr_floor_plan_spinner, parent, false);
        }

        TextView label = (TextView) convertView.findViewById(R.id.spinnerTarget);
        label.setText(mNames.get(position));

        return convertView;
    }

    @Override
    public int getCount() {
        return mNames.size();
    }

    @Override
    public Object getItem(int position) {
        return mNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.fr_floor_plan_spinner, parent, false);
        }

        TextView label = (TextView) convertView.findViewById(R.id.spinnerTarget);
        label.setText(mNames.get(position));

        return convertView;
    }
}
