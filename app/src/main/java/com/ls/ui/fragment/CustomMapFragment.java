package com.ls.ui.fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import android.os.Bundle;


public class CustomMapFragment extends SupportMapFragment {

    public interface OnActivityCreatedListener {
        void onActivityCreated(GoogleMap googleMap);
    }

    private OnActivityCreatedListener mListener;

    public static CustomMapFragment newInstance(OnActivityCreatedListener listener) {
        CustomMapFragment mapFragment = new CustomMapFragment();
        mapFragment.setOnActivityCreatedListener(listener);

        return mapFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mListener != null) {
            mListener.onActivityCreated(getMap());
        }
    }

    private void setOnActivityCreatedListener(OnActivityCreatedListener listener) {
        mListener = listener;
    }
}
