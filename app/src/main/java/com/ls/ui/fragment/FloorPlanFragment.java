package com.ls.ui.fragment;

import com.ls.drupalcon.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created on 09.06.2016.
 */
public class FloorPlanFragment  extends Fragment
{
    public static final String TAG = "FloorPlanFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fr_floor_plan, null);
    }
}
